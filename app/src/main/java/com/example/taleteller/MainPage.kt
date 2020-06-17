package com.example.taleteller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction

import com.example.taleteller.fragments.ListFragment
import com.example.taleteller.fragments.ListFragmentDate
import com.example.taleteller.fragments.ListFragmentOwner
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.header.*
import java.io.File
import java.io.IOException


class MainPage : AppCompatActivity() , OnNavigationItemSelectedListener{



    lateinit var homeFragment: HomeFragment
    lateinit var workFragment: WorkFragment
    lateinit var schoolFragment: SchoolFragment
    lateinit var timelineFragment: TimelineFragment
    lateinit var settingFragment: SettingFragment
    lateinit var logoutFragment: LogoutFragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(toolbBar)
        val actionBar = supportActionBar
        actionBar?.title = "Tale Teller"
        menuValues()





        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbBar,
            (R.string.open),
            (R.string.close)
        ){

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, ListFragment.newInstance(), ListFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit();


    }




    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.home -> {
            homeFragment = HomeFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, ListFragment.newInstance(), ListFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
            R.id.search -> {
            workFragment = WorkFragment()
                supportFragmentManager
                startActivity(Intent(this,searchActivity::class.java))
                finish()
        }

            R.id.school -> {
                schoolFragment = SchoolFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, ListFragmentOwner.newInstance(), ListFragmentOwner.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            }
            R.id.timeline -> {
                timelineFragment = TimelineFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, timelineFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.setting -> {
                settingFragment = SettingFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, settingFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.logout -> {
                logoutFragment = LogoutFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, logoutFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }


    fun menuValues(){
        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl("gs://taleteller-3cc8c.appspot.com/avatar").child(u!!.uid+".jpg")


        val docRef = db.collection("Users").document(u!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    menuUserName.text = document.getString("userName")
                    menuUserEmail.text = document.getString("email")
                } else {

                }
            }
            .addOnFailureListener { exception ->

            }
        try{
            val file = File.createTempFile("image","jpg")
            storageReference.getFile(file).addOnSuccessListener {

                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    avatarMenu.setImageBitmap(bitmap)

            }
        }catch(e : IOException) {
        }

    }




}
