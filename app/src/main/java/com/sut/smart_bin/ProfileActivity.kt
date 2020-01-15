package com.sut.smart_bin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

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


            "https://smart-bin-sut.herokuapp.com/api/User/${uid}".httpGet().responseObject(
                MainActivity.User.Deserializer()
            ) { req, res, result ->
                //result is of type Result<User, Exception>
                val (users, err) = result

                if (users != null) {

                    if (users.uid == uid) {
                        println("***************** GET 2 ***************")
                        var Ids = users.ids
                        var Name = users.name
                        var Email = users.email
                        var Phone = users.phone
                        println(users.uid)
                        fun String.toEditable(): Editable =
                            Editable.Factory.getInstance().newEditable(this)

                        val nametxt = findViewById<EditText>(R.id.id_name_edit)
                        val emailtxt = findViewById<EditText>(R.id.id_email_edit)
                        val phonetxt = findViewById<EditText>(R.id.id_phone_edit)
                        val idstxt = findViewById<EditText>(R.id.id_ids_edit)

                        nametxt.text = Name.toEditable()
                        emailtxt.text = Email.toEditable()
                        phonetxt.text = Phone.toEditable()
                        idstxt.text = Ids.toEditable()

                    } else {

                        println("***************** Test ***************")
                    }
                }
                println("***************** Test 2 ***************")
            }
            println("***************** Test 3 ***************")
        }
        setContentView(R.layout.activity_profile)
        btn_call_api.setOnClickListener {
            println("+++++++++++++++++++++++++++++++++++++++")
            Profile()
        }

    }

    fun Profile() {
        val json = """
 {
            "ids": "B5917471",
            "name": "AoAo",
            "email": "chayanunzaza@gmail.com",
            "phone": "0811845468",
            "point": 100,
            "uid": "PrdtHAlkKWdVVLzTIyt6WaxwUtg1"
        }
""".trimIndent()
        val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smart-bin-sut.herokuapp.com/api/User/${user!!.uid}")
            .jsonBody(json)
            .also { println(it) }
            .response { result -> }
    }



}
