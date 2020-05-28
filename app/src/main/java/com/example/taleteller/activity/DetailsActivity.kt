package com.example.taleteller

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taleteller.R
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.header.*


class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        supportActionBar?.title = user.title

        imageView = findViewById(R.id.image_view)
        textView = findViewById(R.id.text_view)

        imageView.setImageResource(user?.resId)
        textView.setText(user?.content)

        getData()
        owner()
        editButton.setOnClickListener(){
            Search.Companion.titleVar = user.title.toString()
            Search.Companion.shortcutVar = user.shortcut.toString()
            Search.Companion.contentVar = user.content.toString()
            Search.Companion.idVar = user.id.toString()
            startActivity(Intent(this,EditTale::class.java))
            finish()
        }
        likeImg.setOnClickListener(){
            val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
            val u = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            if(user?.owner.toString() != u!!.uid){
                val docRef = db.collection("Users").document(u!!.uid).collection("Liked").document(user?.id.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document.data != null) {

                        } else {
                            val map = hashMapOf("exist" to true)
                            db.collection("Users").document(u!!.uid).collection("Liked").document(user?.id.toString()).set(map).addOnCompleteListener(){
                            }
                            likeImg.setImageResource(R.drawable.ic_action_like1)
                            val likes = user?.likes!!.toInt() + 1

                            db.collection("Tales").document(user?.id.toString()).update("Likes",likes).addOnCompleteListener(){
                            }
                            likeNumber.text = likes.toString()

                        }
                    }
                    .addOnFailureListener { exception ->

                    }

            }
        }

    }

    fun owner(){
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        val u = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Users").document(u!!.uid).collection("Liked").document(user?.id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    likeImg.setImageResource(R.drawable.ic_action_like1)
                } else {

                }
            }
            .addOnFailureListener { exception ->

            }
        if(user?.owner.toString() == u!!.uid){
            likeImg.setImageResource(R.drawable.ic_action_like1)
            editButton.visibility = 1
        }

    }


    fun getData(){
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        val db = FirebaseFirestore.getInstance()
        var userId = "a"
        likeNumber.text = user?.likes

        val docRef = db.collection("Tales").document(user?.id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userId = document.getString("UserID").toString()
                    val docRef2 = db.collection("Users").document(userId)
                    docRef2.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                detailsUserName.text = document.getString("userName")
                            } else {
                            }
                        }
                        .addOnFailureListener { exception ->
                        }
                } else {

                }
            }
            .addOnFailureListener { exception ->
            }
    }

    companion object {
        var TAG = DetailsActivity::class.java.simpleName
        const val EXTRA_USER_MODEL: String = "user"

        fun newIntent(context: Context, user: User): Intent {
            var intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EXTRA_USER_MODEL, user)
            return intent
        }
    }
}
