package com.example.taleteller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search.*

class searchActivity : AppCompatActivity() {

    lateinit var optionCategory: Spinner
    lateinit var optionSort: Spinner
    var categorySearch: String = ""
    var orderSearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        optionCategory = findViewById(R.id.categorySpinner) as Spinner
        val optionsCategory = arrayOf("default","Crime","Horror","Comic","Fantasy")
        optionCategory.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, optionsCategory)
        optionCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                categorySearch = type
            }
        }

        optionSort = findViewById(R.id.sortSpinner) as Spinner
        val optionsSort = arrayOf("Likes","EditDate")
        optionSort.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, optionsSort)
        optionSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                orderSearch = type
            }
        }

        applyFilter.setOnClickListener(){
            Search.Companion.titleVar = titleSearch!!.text.toString()
            Search.Companion.categoryVar = categorySearch
            Search.Companion.orderVar = orderSearch
            startActivity(Intent(this,MainPage::class.java))
            finish()
        }

        cancelFilter.setOnClickListener{
            startActivity(Intent(this,MainPage::class.java))
            finish()
        }
    }
}
