package com.example.contactme

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "contactDB.sqlite", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS contact_table(id INTEGER PRIMARY KEY AUTOINCREMENT, lastname VARCHAR(255), firstname VARCHAR(255), phone VARCHAR(255))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun selectContact(): Cursor? {
        var db = this.readableDatabase
        return db.rawQuery("SELECT * FROM contact_table", null)
    }

    fun insertContact(lastname: String, firstname: String, phone: String): String {
        var db = this.writableDatabase

        var contentValues = ContentValues()
        contentValues.put("lastname", lastname)
        contentValues.put("firstname", firstname)
        contentValues.put("phone", phone)

        var result = db.insert("contact_table", null, contentValues)

        if(result != (-1).toLong()) {
            return "success"
        } else {
            return "fail"
        }
    }

    fun updateContact(id: Int, lastname: String, firstname: String, phone: String): Boolean {
        var db = this.writableDatabase

        var contentValues = ContentValues()
        contentValues.put("lastname", lastname)
        contentValues.put("firstname", firstname)
        contentValues.put("phone", phone)

        return db.update("contact_table", contentValues, "id = ?", arrayOf(id.toString())) > 0
    }

    fun deleteContact(id: Int): Boolean {
        var db = this.writableDatabase
        return db.delete("contact_table", "id = ?", arrayOf(id.toString())) > 0
    }

    fun selectContactFilter(text: String): Cursor {
        val db = this.readableDatabase
        val selectContact =
            "SELECT * FROM contact1 WHERE firstname like '%$text%'"
        return db.rawQuery(selectContact, null)
    }
}