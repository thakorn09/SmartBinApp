package com.sut.smart_bin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var btn_cast : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val user = FirebaseAuth.getInstance().currentUser
        setContentView(R.layout.activity_main)
        val u = Users(0,0, "","", "","", "",0, "" )
        user?.let {

            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            //val emailVerified = user.isEmailVerified
            val uid = user.uid


            //"https://smartbin-sut.herokuapp.com/User/${uid}".httpGet()
                //.responseObject(Deserializer()) { req, res, result ->
                    Fuel.get( "https://smartbin-sut.herokuapp.com/User/${uid}")
                        .responseObject(Deserializer())  { _, _,   result ->
                        val (users,error) = result

                    println(uid)
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
                        u.Point = users.Point
                        u.GoodBin = users.Bin.GoodBin
                        u.BadBin = users.Bin.BadBin


                        /*  val upoint = u.Bin?.BadBin * 10
                          if(u.Point != upoint?.toInt()){
                              u.Point=upoint?.toInt()
                              UpPoint(
                                  u.Name,u.Email,u.Phone,u.Ids,u.Uid,u.Photo,u.Point,u.Bin?.GoodBin,u.Bin?.BadBin
                              )

                          }*/

                        val nametxt = findViewById<TextView>(R.id.id_name)
                        nametxt.text = u.Name

                        val pointtxt = findViewById<TextView>(R.id.id_point)
                       pointtxt.text = u.Point.toString()

                        val img = findViewById<ImageView>(R.id.id_img)
                        val url =  u.Photo
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
                        u.Point = 0

                        val json = """
                                                {
                                                        "Ids": "${u.Ids}",
                                                        "Name": "${u.Name}",
                                                        "Email": "${u.Email}",
                                                        "Phone": "${u.Phone}",
                                                        "Photo": "${u.Photo}",
                                                        "Uid": "${u.Uid}",
                                                        "Point": ${u.Point},
                                                        "Bin":  {
                                                        "GoodBin": 0,
                                                        "BadBin": 0
                                                        } 
                                                        
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
        btn_cast = findViewById(R.id.btn_cast) as Button

        btn_cast!!.setOnClickListener{
            val intent = Intent(this@MainActivity, ScannerActivity::class.java)
            intent.putExtra("USERS", u)
            startActivity(intent)
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

    fun UpPoint(
        name: String,
        email: String,
        phone: String,
        ids: String,
        uid: String,
        photo: String,
        point: Int,
        goodBin: Long?,
        binBin: Long?
    ) { val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smartbin-sut.herokuapp.com/User/${user!!.uid}/${point}")
            .also { println(it) }
            .response { result -> println(result) }
    }
}