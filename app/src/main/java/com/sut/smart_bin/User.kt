package com.sut.smart_bin

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable


data class Bin(
    val BadBin: Long,
    val GoodBin: Long
)
data class User(
    val Bin: Bin,
    val Email: String,
    val Ids: String,
    val Name: String,
    val Phone: String,
    val Photo: String,
    val Point: Long,
    val Uid: String
)

class Deserializer : ResponseDeserializable<User> {
    override fun deserialize(content: String): User = Gson().fromJson(content, User::class.java)
}

//data class Bins(var GoodBin: Long, var BadBin: Long)
data class Users(
    var GoodBin: Long,
    var BadBin: Long,
    var Email: String,
    var Ids: String,
    var Name: String,
    var Phone: String,
    var Photo: String,
    var Point: Long,
    var Uid: String
) : Serializable


