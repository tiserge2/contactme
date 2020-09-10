package com.example.contactme

class CustomModel {
    var id: Int = 0
    lateinit var lastname: String
    lateinit var firstname: String

    constructor(id: Int, lastname: String, firstname: String) {
        this.id = id
        this.lastname = lastname
        this.firstname = firstname
    }


}