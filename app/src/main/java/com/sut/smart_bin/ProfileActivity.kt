package com.sut.smart_bin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        val u = intent.getSerializableExtra("USERS") as Users

        println("***************** GET 2 ***************")
        //val u = Users()
        var Ids = u.ids
        var Uid = u.uid
        var Name = u.name
        var Email = u.email
        var Phone = u.phone
        var Photo = u.photo
        var Point = u.point

        println("Test : ${u.uid}")
        fun String.toEditable(): Editable =
            Editable.Factory.getInstance().newEditable(this)

        val img = findViewById<ImageView>(R.id.id_img)
        val url = if (u.photo != null) "${u.photo}?w=360" else null //1
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

        nametxt.text = Name?.toEditable()
        emailtxt.text = Email?.toEditable()
        phonetxt.text = Phone?.toEditable()
        idstxt.text = Ids?.toEditable()



        btn_call_api.setOnClickListener {
            println("+++++++++++++++++++++++++++++++++++++++")
            Profile(
                nametxt.text,
                emailtxt.text,
                phonetxt.text,
                idstxt.text,
                Uid, Point, Photo
            )
        }

    }

    fun Profile(
        name: Editable,
        email: Editable,
        phone: Editable,
        ids: Editable,
        uid: String,
        point: Long,
        photo: String
    ) {
        val json = """
 {
            "ids": "${ids}",
            "name": "${name}",
            "email": "${email}",
            "phone": "${phone}",
            "point": ${point}, 
            "photo": "${photo}",
            "uid": "${uid}"
        }
""".trimIndent()
        val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smart-bin-sut.herokuapp.com/api/User/${user!!.uid}")
            .jsonBody(json)
            .also { println(it) }
            .response { result -> }
    }


}
