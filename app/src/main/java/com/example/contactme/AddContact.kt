package com.example.contactme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ekontest_hackathon.EmptyField
import org.json.JSONObject
import java.util.*


class AddContact : AppCompatActivity() {
    var lastname: EditText? = null
    var firstname: EditText? = null
    var phone: EditText? = null
    lateinit var requestQueue: RequestQueue
    lateinit var result: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        supportActionBar?.hide()

        lastname = findViewById(R.id.editTextLastname)
        firstname = findViewById(R.id.editTextFirstname)
        phone = findViewById(R.id.editTextPhone)
        requestQueue = Volley.newRequestQueue(this)
    }

    fun saveContactAction(view: View) {
        System.out.println("INSIDE SAVING CONTACT")
        //save the contact
        val collection = arrayListOf<EditText>()
        collection.add(findViewById(R.id.editTextLastname))
        collection.add(findViewById(R.id.editTextFirstname))
        collection.add(findViewById(R.id.editTextPhone))
        var Empty = EmptyField(collection)

        if(Empty.isAllFieldFilled) {
            System.out.println("Adding data")
            addData(lastname?.text.toString(), firstname?.text.toString(), phone?.text.toString())
        } else {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            startActivity(Intent(applicationContext, ContactList::class.java))
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun cancelSaveAction(view: View) {
        //save the contact
        startActivity(Intent(applicationContext, ContactList::class.java))
        finish()
    }

    fun back() {
        //save the contact
        startActivity(Intent(applicationContext, ContactList::class.java))
        finish()
    }

    fun addData(lastname: String, firstname: String, phone: String) {
        System.out.println("Inside Adding data")
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
                        "Contact saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    ContactList.arrayAdapter.notifyDataSetChanged()
                    back()
                } else {
                    Toast.makeText(applicationContext, "Cannot saved contact", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error -> Log.d("There is an error", error.toString()) }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["firstname"] = firstname
                params["lastname"] = lastname
                params["action"] = "insert"
                params["phone"] = phone
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
}