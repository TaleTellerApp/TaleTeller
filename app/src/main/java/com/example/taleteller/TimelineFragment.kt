package com.example.taleteller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class TimelineFragment : Fragment() {
    var addtitle : EditText? =null
    var addshortcut : EditText? =null
    var addcontent : EditText? =null
    val options = arrayOf("Horror","Comic","Fantasy","Crime")
    var categoryTale : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val t=inflater.inflate(R.layout.fragment_timeline, container, false)
        val spinner = t.findViewById<Spinner>(R.id.categorySpin)
        spinner?.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, options) as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                categoryTale = type

            }
        }
        return t
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
        map.set("Category",categoryTale)

        db.collection("Tales").document().set(map).addOnCompleteListener(){
            if(it.exception != null) {
                return@addOnCompleteListener
            }
        }
    }
}
