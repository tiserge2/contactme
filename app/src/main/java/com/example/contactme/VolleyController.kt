package com.example.contactme

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class VolleyController(context: Context) {

    val requestQueue = Volley.newRequestQueue(context)

    fun sendData() {

    }

    fun readData(url: String){
        val list = ArrayList<CustomModel>()

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
                        val id = responseArr.getJSONObject(i).getString("id") as Int
                        val lastname = responseArr.getJSONObject(i).getString("lastname")
                        val firstname = responseArr.getJSONObject(i).getString("firstname")
                        val email = responseArr.getJSONObject(i).getString("email")
                        println("Lastname: $lastname")
                        list.add(CustomModel(id, lastname, firstname, email))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
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