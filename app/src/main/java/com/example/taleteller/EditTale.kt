package com.example.taleteller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.taleteller.DetailsActivity.Companion.EXTRA_USER_MODEL
import com.example.taleteller.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_tale.*

class EditTale : AppCompatActivity() {

    lateinit var optionCategory: Spinner
    var categorySearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tale)
        changeTitle.setText(Search.Companion.titleVar)
        changeContent.setText(Search.Companion.contentVar)
        changeShortcut.setText(Search.Companion.shortcutVar)
        Search.Companion.titleVar = ""

        optionCategory = findViewById(R.id.categorySpinner) as Spinner
        val optionsCategory = arrayOf("Crime","Horror","Comic","Fantasy")
        optionCategory.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, optionsCategory)
        optionCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                categorySearch = type
            }
        }

        changeTale.setOnClickListener(){
            val db = FirebaseFirestore.getInstance()
            db.collection("Tales").document(Search.Companion.idVar.toString()).update("Title",changeTitle.text.toString()).addOnCompleteListener(){
            }
            db.collection("Tales").document(Search.Companion.idVar.toString()).update("Category",categorySearch).addOnCompleteListener(){
            }
            db.collection("Tales").document(Search.Companion.idVar).update("Shortcut",changeShortcut.text.toString()).addOnCompleteListener(){
            }
            db.collection("Tales").document(Search.Companion.idVar).update("Content",changeContent.text.toString()).addOnCompleteListener(){
            }
            startActivity(Intent(this,MainPage::class.java))
            finish()
        }
    }
}
