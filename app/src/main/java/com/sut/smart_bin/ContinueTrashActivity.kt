package com.sut.smart_bin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_continue_trash.*
import kotlinx.android.synthetic.main.activity_profile.*

class ContinueTrashActivity : AppCompatActivity() {

    //private var btn_YesTrash : Button? = null
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_trash)
        val user = FirebaseAuth.getInstance().currentUser
        val sCan = intent.getSerializableExtra("scan")






        btn_NoTrash.setOnClickListener {
            Toast.makeText(applicationContext,"Finish",Toast.LENGTH_SHORT).show()
            Fuel.put("https://smartbin-sut.herokuapp.com/SmartBin/${sCan}/${user!!.uid}/0")
                .also { println(it) }
                .response { result -> }
            moveMainPage()
        }


        btn_YesTrash.setOnClickListener {
            Toast.makeText(applicationContext,"State2: "+sCan,Toast.LENGTH_SHORT).show()
            Fuel.put("https://smartbin-sut.herokuapp.com/SmartBin/${sCan}/${user!!.uid}/2")
                .also { println(it) }
                .response { result -> }



            val dialogBuilder = AlertDialog.Builder(this)

            dialogBuilder.setTitle("Warning")
            dialogBuilder.setPositiveButton("OK") {dialog, which ->
            }
            dialogBuilder.setMessage("Please wait for the machine to finish working. If you want to continue disposing, please click Yes. If not please click No.")
            val alert =  dialogBuilder.create()
            println(alert)
            alert.show()

        }
        //btn_YesTrash = findViewById(R.id.btn_YesTrash) as Button

       /* btn_YesTrash!!.setOnClickListener{
            val intent = Intent(this@ContinueTrashActivity, ScannerActivity::class.java)
            startActivity(intent)
        }*/
    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
