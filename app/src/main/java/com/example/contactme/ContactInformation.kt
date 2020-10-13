package com.example.contactme

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.HashMap

class ContactInformation : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_information)
        supportActionBar?.hide()
        requestQueue = Volley.newRequestQueue(this)

        val model = intent.getSerializableExtra("model") as CustomModel
        findViewById<TextView>(R.id.textViewLastname).setText(model.lastname)
        findViewById<TextView>(R.id.textViewFirstname).setText(model.firstname)
        findViewById<TextView>(R.id.textViewPhone).setText(model.phone)
    }

    fun editContactAction(view: View) {
        val model = intent.getSerializableExtra("model") as CustomModel
        var intent = Intent(applicationContext, EditContact::class.java)
        intent.putExtra("model", model)
        startActivity(intent)
        finish()
    }

    fun deleteInformationAction(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                val model = intent.getSerializableExtra("model") as CustomModel
                var id = model.id
                removeContact(id)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    fun removeContact(id: Int) {
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
                        "Contact deleted successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    ContactList.arrayAdapter.notifyDataSetChanged()
                    back()
                } else {
                    Toast.makeText(applicationContext, "Cannot delete contact", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error -> Log.d("There is an error", error.toString()) }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = id.toString()
                params["action"] = "delete"
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
            startActivity(Intent(applicationContext, ContactList::class.java))
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