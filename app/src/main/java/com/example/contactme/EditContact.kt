package com.example.contactme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ekontest_hackathon.EmptyField
import org.json.JSONObject
import java.util.HashMap

class EditContact : AppCompatActivity() {
//    lateinit val lastname : EditText? = null
//    val firstname : EditText? = null
//    val phone : EditText? = null
    lateinit var requestQueue: RequestQueue
    lateinit var model: CustomModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        supportActionBar?.hide()


    val model = intent.getSerializableExtra("model") as CustomModel
        var lastname = findViewById<EditText>(R.id.editTextLastname)
        var firstname = findViewById<EditText>(R.id.editTextFirstname)
        var phone = findViewById<EditText>(R.id.editTextPhone)
        requestQueue = Volley.newRequestQueue(this)

        lastname.setText(model.lastname)
        firstname.setText(model.firstname)
        phone.setText(model.phone)
    }

    fun editContactAction(view: View) {
        model = intent.getSerializableExtra("model") as CustomModel
        var id = model.id as Int
        var lastname = findViewById<EditText>(R.id.editTextLastname).text.toString()
        var firstname = findViewById<EditText>(R.id.editTextFirstname).text.toString()
        var phone = findViewById<EditText>(R.id.editTextPhone).text.toString()

        val db = DatabaseHelper(this)
        val collection = arrayListOf<EditText>()
        collection.add(findViewById(R.id.editTextLastname))
        collection.add(findViewById(R.id.editTextFirstname))
        collection.add(findViewById(R.id.editTextPhone))
        var Empty = EmptyField(collection)

        if(Empty.isAllFieldFilled) {
            editContact(id.toString(), lastname, firstname, phone)
        }
    }

    fun cancelEditAction(view: View) {
        model = intent.getSerializableExtra("model") as CustomModel
        val intent = Intent(applicationContext, ContactInformation::class.java)
        intent.putExtra("model", model)
        startActivity(intent)
        finish()
    }

    fun editContact(id: String, lastname: String, firstname: String, phone: String) {
        val url = "http://192.168.43.4:80/contact/controller.php"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener {
                val jsonResponse = JSONObject(it)
                val result: String = jsonResponse.getString("response")
                System.out.println("Response from server: " + result)

                if (result == "success") {
                    Toast.makeText(
                        applicationContext,
                        "Contact edited successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    ContactList.arrayAdapter.notifyDataSetChanged()
                    back()
                } else {
                    Toast.makeText(applicationContext, "Cannot edit contact", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error -> Log.d("There is an error", error.toString()) }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["firstname"] = firstname
                params["lastname"] = lastname
                params["action"] = "update"
                params["phone"] = phone
                params["id"]  = id
                return params
            }
        }
        postRequest.retryPolicy =
            DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        requestQueue.add(postRequest)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            model = intent.getSerializableExtra("model") as CustomModel
            val intent = Intent(applicationContext, ContactInformation::class.java)
            intent.putExtra("model", model)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun back() {
        //save the contact
        startActivity(Intent(applicationContext, ContactList::class.java))
        finish()
    }
}