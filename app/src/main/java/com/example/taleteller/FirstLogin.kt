package com.example.taleteller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_first_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class FirstLogin : AppCompatActivity() {
    var username : EditText ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)
        Log.d( "Problem","Problem")

        saveUsername.setOnClickListener(){
            saveUsername()
            startActivity(Intent(this,MainPage::class.java))
        }
    }

    fun saveUsername(){
        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        val map = hashMapOf(
            "userName" to inputUsername!!.text.toString(),
            "email" to u!!.email.toString(),
            "firstLogin" to Date()
        )
        val map2 = hashMapOf("exist" to true)



        db.collection("Users").document(u!!.uid).set(map).addOnCompleteListener(){
            if(it.exception != null) {
                return@addOnCompleteListener
            }
        }
        db.collection("Users").document(u!!.uid).collection("Liked").document("test").set(map2).addOnCompleteListener(){
        }


    }
}
