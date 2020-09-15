package com.example.contactme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ekontest_hackathon.EmptyField

class EditContact : AppCompatActivity() {
//    lateinit val lastname : EditText? = null
//    val firstname : EditText? = null
//    val phone : EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        supportActionBar?.hide()


    val model = intent.getSerializableExtra("model") as CustomModel
        var lastname = findViewById<EditText>(R.id.editTextLastname)
        var firstname = findViewById<EditText>(R.id.editTextFirstname)
        var phone = findViewById<EditText>(R.id.editTextPhone)

        lastname.setText(model.lastname)
        firstname.setText(model.firstname)
        phone.setText(model.phone)
    }

    fun editContactAction(view: View) {
        val model = intent.getSerializableExtra("model") as CustomModel
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
            var result = db.updateContact(id, lastname, firstname, phone)

            if(result) {
                Toast.makeText(applicationContext, "Contact edited successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext, ContactList::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, "Cannot edit contact", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelEditAction(view: View) {
        startActivity(Intent(applicationContext, ContactList::class.java))
        finish()
    }
}