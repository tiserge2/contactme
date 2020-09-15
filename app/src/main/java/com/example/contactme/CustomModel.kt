package com.example.contactme

import java.io.Serializable

class CustomModel: Serializable {
    var id: Int = 0
    lateinit var lastname: String
    lateinit var firstname: String
    lateinit var phone: String

    constructor(id: Int, lastname: String, firstname: String, phone: String) {
        this.id = id
        this.lastname = lastname
        this.firstname = firstname
        this.phone = phone
    }
}