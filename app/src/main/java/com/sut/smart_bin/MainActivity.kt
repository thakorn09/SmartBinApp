package com.sut.smart_bin

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val user = FirebaseAuth.getInstance().currentUser
        setContentView(R.layout.activity_main)
        val u = Users()
        user?.let {

            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val emailVerified = user.isEmailVerified
            val uid = user.uid


            "https://smart-bin-sut.herokuapp.com/api/User/${uid}".httpGet()
                .responseObject(Deserializer()) { req, res, result ->
                    val (users, err) = result

                    println(uid)
                    println(user.uid)

                    if (users != null && users.uid == uid) {
                        println("***************** GET-User ***************")
                        /*val u = User(
                            users.id,
                            users.uid,
                            users.ids,
                            users.name,
                            users.email,
                            users.phone,
                            users.photo,
                            users.point
                        )*/

                        u.uid = users.uid
                        u.ids = users.ids
                        u.name = users.name
                        u.email = users.email
                        u.phone = users.phone
                        u.photo = users.photo
                        u.point = users.point


                        val nametxt = findViewById<TextView>(R.id.id_name)
                        nametxt.text = u.name

                        val pointtxt = findViewById<TextView>(R.id.id_point)
                        pointtxt.text = u.point.toString()

                        val img = findViewById<ImageView>(R.id.id_img)
                        val url = if (u.photo != null) "${u.photo}?w=360" else null //1
                        Glide.with(img)
                            .load(url)
                            .override(300, 300)
                            .centerCrop()
                            .transform(CircleCrop())
                            .into(img)

                        println(result)
                    } else {
                        /*val u = Users(
                            "",
                            uid,
                            "",
                            name.toString(),
                            email.toString(),
                            "",
                            photoUrl.toString(),
                            0
                        )*/


                        u.uid = uid
                        u.ids = ""
                        u.name = name.toString()
                        u.email = email.toString()
                        u.phone = ""
                        u.photo = photoUrl.toString()
                        u.point = 0

                        val json = """
                                             {
                                                        "ids": "${u.ids}",
                                                        "name": "${u.name}",
                                                        "email": "${u.email}",
                                                        "phone": "${u.phone}",
                                                        "point": ${u.point},
                                                        "photo": "${u.photo}",
                                                        "uid": "${u.uid}"
                                                }
                                            """.trimIndent()

                        println("***************** POST-User ***************")
                        Fuel.post("https://smart-bin-sut.herokuapp.com/api/User")
                            .jsonBody(json)
                            .also { println(it) }
                            .response { result ->
                                println(result)
                            }
                    }

                }
        }

        btn_sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            moveMainPage()
        }

        btn_edit_proflie.setOnClickListener {
            moveProfile(u)

        }

    }

    fun moveProfile(u: Users) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("USERS", u)
        startActivity(intent)

    }

    fun moveMainPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

    }


}