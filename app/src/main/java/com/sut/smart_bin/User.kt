package com.sut.smart_bin

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable


data class User(val id: String = "",
                val ids: String = "",
                val uid: String = "",
                val name: String = "",
                val email: String = "",
                val phone: String = "",
                var photo: String = "",
                val point: Long = 0)


    class Deserializer : ResponseDeserializable<User> {
        override fun deserialize(content: String) = Gson().fromJson(content, User::class.java)
    }


class Users : Serializable {

    var uid: String = ""
    var ids: String = ""
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var photo: String = ""
    var point: Long = 0
}

