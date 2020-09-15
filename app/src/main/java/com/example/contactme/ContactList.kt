package com.example.contactme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.*
import java.io.Serializable

class ContactList : AppCompatActivity() {
    var count: Int? = null
    lateinit var arrayAdapter: CustomAdapter
    var db = DatabaseHelper(this)
    var arrayList = arrayListOf<CustomModel>()
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        listView = findViewById<ListView>(R.id.listView)
        var cursor = db.selectContact()

        if (cursor != null) {
            while(cursor.moveToNext()) {
                arrayList.add(CustomModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }
        }

        count = arrayList.size

        arrayAdapter = CustomAdapter(applicationContext, R.layout.custom_listview, arrayList)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position:Int, id:Long) {
                val pos = position
                val value = listView.getItemAtPosition(pos) as CustomModel
                var intent  = Intent(applicationContext, ContactInformation::class.java)
                intent.putExtra("model", value)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        var searchItem = menu?.findItem(R.id.app_bar_search)
        var searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                val text = p0
                System.out.println("Text string: " + text)
                arrayAdapter.filter(text)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun addContactAction(view: View) {
        var intent = Intent(applicationContext, AddContact::class.java)
        startActivity(intent)
    }

    fun informationAction(view: View) {
        //count the number of contact present
        System.out.println("helllooo... " + count)
        Toast.makeText(applicationContext, "You have " + count + " contact(s).", Toast.LENGTH_LONG).show()
    }
}