package com.sut.smart_bin

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable
import java.sql.Array
import java.util.*


interface Bins {
    val GoodBin: Long
    val BadBin: Long
}

data class User(
    val Id: String = "",
    val Ids: String = "",
    val Uid: String = "",
    val Name: String = "",
    val Email: String = "",
    val Phone: String = "",
    val Photo: String = "",
    val Point: Int = 0,
    override val GoodBin: Long = 0,
    override val BadBin: Long = 0

) : Bins


class Deserializer : ResponseDeserializable<User> {
    override fun deserialize(content: String) = Gson().fromJson(content, User::class.java)
}


class Users : Serializable {

    var Uid: String = ""
    var Ids: String = ""
    var Name: String = ""
    var Email: String = ""
    var Phone: String = ""
    var Photo: String = ""
    var Point: Int = 0
    var Bin = Bin(0,0)

}

class Bin (
    var GoodBin: Long = 0,
    var BadBin: Long = 0
) : Serializable

