package com.sut.smart_bin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        val sCan = intent.getSerializableExtra("scan")




        listenToMultiple()

        btn_NoTrash.setOnClickListener {
            Toast.makeText(applicationContext,"State2: "+sCan,Toast.LENGTH_SHORT).show()
            Fuel.put("https://smartbin-sut.herokuapp.com/SmartBin/${sCan}/0")
                .also { println(it) }
                .response { result -> }
            moveMainPage()
        }

        btn_YesTrash.setOnClickListener {
            Toast.makeText(applicationContext,"State2: "+sCan,Toast.LENGTH_SHORT).show()
            Fuel.put("https://smartbin-sut.herokuapp.com/SmartBin/${sCan}/2")
                .also { println(it) }
                .response { result -> }
        }

        //btn_YesTrash = findViewById(R.id.btn_YesTrash) as Button

       /* btn_YesTrash!!.setOnClickListener{
            val intent = Intent(this@ContinueTrashActivity, ScannerActivity::class.java)
            startActivity(intent)
        }*/


    }


    private fun listenToMultiple() {
        val docRef = db.collection("SmartBin").document("State")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                //Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Toast.makeText(applicationContext,"Current data:"+snapshot.data,Toast.LENGTH_SHORT).show()
                //Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                Toast.makeText(applicationContext,"Current data: null",Toast.LENGTH_SHORT).show()
                //Log.d(TAG, "Current data: null")
            }
        }

    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }



}
