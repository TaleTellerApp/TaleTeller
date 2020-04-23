package com.example.taleteller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeUsername.setOnClickListener(){
            val intent = Intent(activity, FirstLogin::class.java)
            startActivity(intent)
        }

        changePassword.setOnClickListener(){

        }
        settingValues()


    }

    fun settingValues(){

        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        val docRef = db.collection("Users").document(u!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    username.text = document.getString("userName")
                    email.text = document.getString("email")
                } else {

                }
            }
            .addOnFailureListener { exception ->

            }
    }



}
