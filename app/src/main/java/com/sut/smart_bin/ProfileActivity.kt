package com.sut.smart_bin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val u = intent.getSerializableExtra("USERS") as Users

        println("***************** GET 2 ***************")
        //val u = Users()
        var Ids = u.Ids
        var Uid = u.Uid
        var Name = u.Name
        var Email = u.Email
        var Phone = u.Phone
        var Photo = u.Photo

        println("Test : ${u.Uid}")
        fun String.toEditable(): Editable =
            Editable.Factory.getInstance().newEditable(this)

        val img = findViewById<ImageView>(R.id.id_img)
        val url = if (u.Photo != null) "${u.Photo}?w=360" else null //1
        Glide.with(img)
            .load(url)
            .override(300, 300)
            .centerCrop()
            .transform(CircleCrop())
            .into(img)

        val nametxt = findViewById<EditText>(R.id.id_name_edit)
        val emailtxt = findViewById<EditText>(R.id.id_email_edit)
        val phonetxt = findViewById<EditText>(R.id.id_phone_edit)
        val idstxt = findViewById<EditText>(R.id.id_ids_edit)

        nametxt.text = Name.toEditable()
        emailtxt.text = Email.toEditable()
        phonetxt.text = Phone.toEditable()
        idstxt.text = Ids.toEditable()


        btn_call_api.setOnClickListener {
            println("+++++++++++++++++++++++++++++++++++++++")
            Profile(
                nametxt.text,
                emailtxt.text,
                phonetxt.text,
                idstxt.text,
                Uid, Photo,
                u.Bin.GoodBin,u.Bin.BadBin
            )
        }


    }

    fun Profile(
        name: Editable,
        email: Editable,
        phone: Editable,
        ids: Editable,
        uid: String,
        photo: String,
        goodBin: Long,
        binBin: Long
    ) {
        val json = """
 {
            "Ids": "${ids}",
            "Name": "${name}",
            "Email": "${email}",
            "Phone": "${phone}",
            "Photo": "${photo}",
            "Uid": "${uid}",
             "Bin" :         [
                        {
                    "GoodBin" : ${goodBin},
                    "BadBin" : ${binBin}
                        }
                    ]
        }
""".trimIndent()
        val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smartbin-sut.herokuapp.com/User/${user!!.uid}")
            .jsonBody(json)
            .also { println(it) }
            .response { result -> }

        Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()


    }




}
