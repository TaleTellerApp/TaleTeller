package com.example.taleteller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        registerbutton.setOnClickListener() {
            signUpUser()
        }
        backbutton.setOnClickListener(){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
        private fun signUpUser(){
            if(registeremail.text.toString().isEmpty()){
                registeremail.error = "Please enter email"
                registeremail.requestFocus()
                return
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(registeremail.text.toString()).matches()){
                registeremail.error = "Please enter email"
                registeremail.requestFocus()
                return
            }
            if(registerpassword.text.toString().isEmpty()){
                registerpassword.error = "Please enter password"
                registerpassword.requestFocus()
                return
            }
            auth.createUserWithEmailAndPassword(registeremail.text.toString(), registerpassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()
                                }
                            }

                    } else {
                        Toast.makeText(baseContext, "Sing Up failed. Try again later",
                            Toast.LENGTH_SHORT).show()

                    }

                    // ...
                }
    }
}
