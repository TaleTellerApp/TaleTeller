package com.example.taleteller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class TimelineFragment : Fragment() {
    var addtitle : EditText? =null
    var addshortcut : EditText? =null
    var addcontent : EditText? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addTale.setOnClickListener(){
            saveTale()
            val intent = Intent(activity, MainPage::class.java)
            startActivity(intent)

        }


    }
    fun saveTale(){
        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        val map = HashMap<String,Any>()

        map.set("Title",addTitle!!.text.toString())
        map.set("Shortcut",addShortcut!!.text.toString())
        map.set("Content", addContent!!.text.toString())
        map.set("Likes", 0)
        map.set("UserID", u!!.uid)
        map.set("EditDate", Date())

        db.collection("Tales").document().set(map).addOnCompleteListener(){
            if(it.exception != null) {
                Log.d("Duzy", "Problem")
                return@addOnCompleteListener
            }
        }
    }
}
