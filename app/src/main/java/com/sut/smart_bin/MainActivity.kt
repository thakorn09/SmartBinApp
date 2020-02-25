package com.sut.smart_bin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var btn_cast : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        val user = FirebaseAuth.getInstance().currentUser
        val userid = FirebaseAuth.getInstance().getUid().toString()
        setContentView(R.layout.activity_main)





        val u = Users(0,0, "","", "","", "",0, "" )
        user?.let {

            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val uid = userid

            //val emailVerified = user.isEmailVerified


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


                          val upoint = u.GoodBin * 10
                          if(u.Point != upoint){
                              u.Point=upoint
                              UpPoint(u.Point)
                          }

                        val recycle = u.GoodBin.toFloat()
                        val nonrecycle = u.BadBin.toFloat()

                        val totaltrash = recycle+nonrecycle

                        val avgrecycle = (recycle/totaltrash)*100
                        val avgnonrecycle = (nonrecycle/totaltrash)*100

                        setUpPieChartData(avgrecycle,avgnonrecycle)



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



        val navigationView = findViewById<View>(R.id.nav) as BottomNavigationView
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.home -> Toast.makeText(applicationContext, "You are Home Page", Toast.LENGTH_SHORT).show()

                R.id.profile -> moveProfile(u)

                R.id.signout -> moveMainPage()
            }
            true
        }

        btn_cast = findViewById(R.id.btn_cast) as Button

        btn_cast!!.setOnClickListener{
            val intent = Intent(this@MainActivity, ScannerActivity::class.java)
            intent.putExtra("USERS", u)
            startActivity(intent)
        }
    }

    private fun setUpPieChartData(avgrecycle:Float ,avgnonrecycle:Float) {
        val R =avgrecycle
        val NR = avgnonrecycle
        if (R.toInt()!=0||NR.toInt()!=0){
        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(avgrecycle, "Can Recycle"))
        yVals.add(PieEntry(avgnonrecycle, "Can not Recycle"))


        val dataSet = PieDataSet(yVals, "")

        dataSet.valueTextSize = 15f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#86ff70"))
        colors.add(Color.parseColor("#ffb126"))
        dataSet.setColors(colors)
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.centerTextRadiusPercent = 100f
        pieChart.isDrawHoleEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
            pieChart.setEntryLabelColor(Color.BLACK)
            pieChart.transparentCircleRadius=25f
            pieChart.animateXY(1500,1500)
         }
    }

    fun moveProfile(u: Users) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("USERS", u)
        startActivity(intent)

    }

    fun moveMainPage() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun UpPoint(
        point: Long
    ) { val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smartbin-sut.herokuapp.com/User/${user!!.uid}/${point}")
            .also { println(it) }
            .response { result -> println(result) }
    }
}