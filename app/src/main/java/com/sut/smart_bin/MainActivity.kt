package com.sut.smart_bin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
            // Name, email address, and profile photo Url
            val name = user?.displayName
            val email = user?.email
            val photoUrl = user?.photoUrl

            // Check if user's email is verified
            val emailVerified = user?.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user?.uid


        setContentView(R.layout.activity_main)
        btn_sign_out.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            moveMainPage();
        }
        Log.d("TAG", "User profile updated." + name)
        println("OK")

    }

    fun moveMainPage(){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

    }
}