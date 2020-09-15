package com.example.contactme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList


class CustomAdapter(context: Context, var ressource: Int, var objects: ArrayList<CustomModel>):
    ArrayAdapter<CustomModel>(context, ressource, objects) {
    lateinit var arrayList: ArrayList<CustomModel>
    var tempList = ArrayList(objects)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layoutInflater = LayoutInflater.from(context)
        var convertView = layoutInflater.inflate(ressource, null, false)

        var lastname = convertView.findViewById<TextView>(R.id.textViewLastname)
        var firstname = convertView.findViewById<TextView>(R.id.textViewFirstname)
        var phone = convertView.findViewById<TextView>(R.id.textViewPhone)

        lastname.text   = objects[position].lastname
        firstname.text  = objects[position].firstname
        phone.text      = objects[position].phone


        return convertView
    }

    fun filter(charText: String) {
        System.out.println("Now in filter")
        val charText = charText!!.toLowerCase(Locale.getDefault())
        System.out.println("orig 0 list size: " + objects.size)

        objects.clear()
        if (charText.length == 0) {
            /*If Search query is Empty than we add all temp data into our main ArrayList
            We store Value in temp in Starting of Program.
            */
            System.out.println("Now in filter, no string ")
            objects.addAll(tempList)
        } else {
            System.out.println("Now filtering")
            System.out.println("Temp list size: " + tempList.size)
            System.out.println("orig list size: " + objects.size)
            for (i in 0..tempList.size - 1) {
                /*
                If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                */
                if (tempList.get(i).lastname!!.toLowerCase(Locale.getDefault()).contains(charText) ||
                    tempList.get(i).firstname!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    objects.add(tempList.get(i))
                }
            }
        }
        //This is to notify that data change in Adapter and Reflect the changes.
        notifyDataSetChanged()
    }
}