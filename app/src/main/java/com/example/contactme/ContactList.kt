package com.example.contactme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.*
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.io.Serializable

class ContactList : AppCompatActivity() {
    var count: Int? = null
    companion object {
        lateinit var arrayAdapter: CustomAdapter
    }
    var arrayList = arrayListOf<CustomModel>()
    lateinit var listView: ListView
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        listView = findViewById<ListView>(R.id.listView)
        requestQueue = Volley.newRequestQueue(this)
        arrayAdapter = CustomAdapter(applicationContext, R.layout.custom_listview, arrayList)
        listView.adapter = arrayAdapter

        //new part
        readData()
        //end of new part

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position:Int, id:Long) {
                val pos = position
                val value = listView.getItemAtPosition(pos) as CustomModel
                var intent  = Intent(applicationContext, ContactInformation::class.java)
                intent.putExtra("model", value)
                startActivity(intent)
                finish()
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
        finish()
    }

    fun informationAction(view: View) {
        //count the number of contact present
        System.out.println("helllooo... " + count)
        Toast.makeText(applicationContext, "You have " + count + " contact(s).", Toast.LENGTH_LONG).show()
    }

    fun readData(){
        var url = "http://192.168.214.101:80/contact/controller.php?action=select"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                var responseArr = JSONArray()
                try {
                    responseArr = response.getJSONArray("contact_tb")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                for (i in 0 until responseArr.length()) {
                    try {
                        val id = Integer.valueOf(responseArr.getJSONObject(i).getString("id"))
                        val lastname = responseArr.getJSONObject(i).getString("lastname")
                        val firstname = responseArr.getJSONObject(i).getString("firstname")
                        val phone = responseArr.getJSONObject(i).getString("phone")
                        println("Lastname: $lastname")
                        arrayList.add(CustomModel(id, lastname, firstname, phone))
                        arrayAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                count = arrayList.size
            },
            { error ->
                // TODO: Handle error
                println(error)
            }
        )

        jsonObjectRequest.retryPolicy =
            DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        requestQueue.add(jsonObjectRequest)
    }
}