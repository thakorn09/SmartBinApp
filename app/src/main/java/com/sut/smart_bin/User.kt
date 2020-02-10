package com.sut.smart_bin

import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable
import java.sql.Array
import java.util.*


/*abstract class  Bins {
    abstract val GoodBin: Long
    abstract val BadBin: Long
}

data class User(
    val Id: String = "",
    val Ids: String = "",
    val Uid: String = "",
    val Name: String = "",
    val Email: String = "",
    val Phone: String = "",
    val Photo: String = "",
    val Point: Int,
    override val GoodBin: Long ,
    override val BadBin: Long

) : Bins()*/


class Deserializer : ResponseDeserializable<Users> {
    override fun deserialize(content: String) = Gson().fromJson(content, Users::class.java)
}


class Users : Serializable {

    var Uid: String = ""
    var Ids: String = ""
    var Name: String = ""
    var Email: String = ""
    var Phone: String = ""
    var Photo: String = ""
    var Point: Int = 0
    val Bin : Bin? = null
}

class Bin (
    var GoodBin: Long?,
    var BadBin: Long?
)


