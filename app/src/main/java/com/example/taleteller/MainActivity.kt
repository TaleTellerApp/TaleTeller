package com.example.taleteller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        gotoregisterbutton.setOnClickListener(){
            startActivity(Intent(this,Register::class.java))
            finish()
        }

        loginbutton.setOnClickListener{
            doLogin()
        }
    }

    private fun doLogin(){
        if(loginemail.text.toString().isEmpty()){
            loginemail.error = "Please enter email"
            loginemail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(loginemail.text.toString()).matches()){
            loginemail.error = "Please enter email"
            loginemail.requestFocus()
            return
        }
        if(loginpassword.text.toString().isEmpty()){
            loginpassword.error = "Please enter password"
            loginpassword.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(loginemail.text.toString(), loginpassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                }

            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser : FirebaseUser?){
        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainPage::class.java))
                finish()
            }
            else{
                Toast.makeText(baseContext, "Please verify your email address",
                    Toast.LENGTH_SHORT).show()

            }
        }
        else{

        }

    }
}
