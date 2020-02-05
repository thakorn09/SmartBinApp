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
import com.github.kittinunf.fuel.json.jsonDeserializer
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
            //val emailVerified = user.isEmailVerified
            val uid = user.uid


            //"https://smartbin-sut.herokuapp.com/User/${uid}".httpGet()
                //.responseObject(Deserializer()) { req, res, result ->
                    Fuel.get( "https://smartbin-sut.herokuapp.com/User/${uid}")
                        .responseObject(Deserializer())  {  result ->
                        val (users,error) = result

                    println(uid)
                            println(error)
                    println(result)
                    println("User" + user.uid)

                    if (users != null && users.Uid == uid) {
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

                        u.Uid = users.Uid
                        u.Ids = users.Ids
                        u.Name = users.Name
                        u.Email = users.Email
                        u.Phone = users.Phone
                        u.Photo = users.Photo
                        u.Bin.GoodBin = users.GoodBin
                        u.Bin.BadBin = users.BadBin



                        val nametxt = findViewById<TextView>(R.id.id_name)
                        nametxt.text = u.Name

                        //val pointtxt = findViewById<TextView>(R.id.id_point)
                       // pointtxt.text = u.Point.toString()

                        val img = findViewById<ImageView>(R.id.id_img)
                        val url = if (u.Photo != null) "${u.Photo}?w=360" else null //1
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


                        u.Uid = uid
                        u.Ids = ""
                        u.Name = name.toString()
                        u.Email = email.toString()
                        u.Phone = ""
                        u.Photo = photoUrl.toString()
                        u.Bin.GoodBin = 0
                        u.Bin.BadBin  = 0

                        val json = """
                                             {
                                                        "Ids": "${u.Ids}",
                                                        "Name": "${u.Name}",
                                                        "Email": "${u.Email}",
                                                        "Phone": "${u.Phone}",
                                                        "Photo": "${u.Photo}",
                                                        "Uid": "${u.Uid}",
                                                        "Bin": [
                                                            {
                                                            "GoodBin" : ${u.Bin.GoodBin},
                                                            "BadBin" : ${u.Bin.BadBin}
                                                                }
                                                            ]
                                                }
                                            """.trimIndent()

                        println("***************** POST-User ***************")
                        Fuel.post("https://smartbin-sut.herokuapp.com/User")
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