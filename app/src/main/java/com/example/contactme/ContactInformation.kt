package com.example.contactme

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class ContactInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_information)
        supportActionBar?.hide()

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
    }

    fun deleteInformationAction(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                var db = DatabaseHelper(this)
                val model = intent.getSerializableExtra("model") as CustomModel
                var result = db.deleteContact(model.id)
                if(result) {
                    Toast.makeText(applicationContext, "Contact deleted successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, ContactList::class.java))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Cannot delete contact", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}