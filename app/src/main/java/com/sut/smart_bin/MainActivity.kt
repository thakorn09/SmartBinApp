package com.sut.smart_bin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {

            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid


            "https://smart-bin-sut.herokuapp.com/api/User/${uid}".httpGet().responseObject(User.Deserializer()) { req, res, result ->
                //result is of type Result<User, Exception>
                val (users, err) = result

                if (users != null) {

                    if (users.uid == uid) {
                        println("***************** GET ***************")
                        var Ids = users.ids
                        var Name = users.name
                        var Email = users.email
                        var Phone = users.phone
                        var Point = users.point
                        println(users.uid)

                        val nametxt = findViewById (R.id.id_name) as TextView
                        nametxt.text = Name
                        val pointtxt = findViewById (R.id.id_point) as TextView
                        pointtxt.text = Point.toString()
                    }
                }else{
                    println("***************** POST ***************")
                    Fuel.post("https://smart-bin-sut.herokuapp.com/api/User")
                        .jsonBody("{ \"uid\" : \"${uid}\" }")
                        .also { println(it) }
                        .response { result -> }

                }
            }
        }

        setContentView(R.layout.activity_main)
        btn_sign_out.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            moveMainPage()
        }
        // Log.d("TAG", "User profile updated." + name)
        println("OK")

        btn_edit_proflie.setOnClickListener {
            moveProfile()
        }

    }
    data class User(val id: String = "",
                    val ids: String = "",
                    val uid: String = "",
                    val name: String = "",
                    val email: String = "",
                    val phone: String = "",
                    val point: Long = 0) {
        class Deserializer : ResponseDeserializable<User> {
        override fun deserialize(content: String) = Gson().fromJson(content, User::class.java)
    } }



    
    fun moveProfile(){
        startActivity(Intent(this,ProfileActivity::class.java))

    }
    fun moveMainPage(){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

    }
}