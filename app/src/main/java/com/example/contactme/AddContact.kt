package com.example.contactme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.ekontest_hackathon.EmptyField

class AddContact : AppCompatActivity() {
    var lastname: EditText? = null
    var firstname: EditText? = null
    var phone: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        supportActionBar?.hide()

        lastname = findViewById(R.id.editTextLastname)
        firstname = findViewById(R.id.editTextFirstname)
        phone = findViewById(R.id.editTextPhone)
    }

    fun saveContactAction(view: View) {
        //save the contact
        var db = DatabaseHelper(this)
        val collection = arrayListOf<EditText>()
        collection.add(findViewById(R.id.editTextLastname))
        collection.add(findViewById(R.id.editTextFirstname))
        collection.add(findViewById(R.id.editTextPhone))
        var Empty = EmptyField(collection)

        if(Empty.isAllFieldFilled) {
            var result = db.insertContact(lastname?.text.toString(), firstname?.text.toString(), phone?.text.toString())

            if(result == "success") {
                Toast.makeText(applicationContext, "Contact saved successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext, ContactList::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, "Cannot saved contact", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelSaveAction(view: View) {
        //save the contact
        startActivity(Intent(applicationContext, ContactList::class.java))
        finish()
    }
}