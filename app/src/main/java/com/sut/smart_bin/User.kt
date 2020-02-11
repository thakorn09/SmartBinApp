package com.sut.smart_bin

import android.os.Parcel
import android.os.Parcelable
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Array
import java.util.*


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

class Users : Serializable {

    var Uid: String = ""
    var Ids: String = ""
    var Name: String = ""
    var Email: String = ""
    var Phone: String = ""
    var Photo: String = ""
    var Point: Long = 0
    val Bin: Bins? = null
}

class Bins(
    var GoodBin: Long?,
    var BadBin: Long?
)


