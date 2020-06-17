package com.example.taleteller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_first_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import java.util.*
import kotlin.collections.HashMap

class FirstLogin : AppCompatActivity() {
    lateinit var filepath : Uri
    var username : EditText ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)
        Log.d( "Problem","Problem")

        saveUsername2.setOnClickListener{
            saveUsername()
            uploadFile()

        }
        button2.setOnClickListener{
            startFileChooser()
        }
    }
    fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }
    fun uploadFile(){
        val u = FirebaseAuth.getInstance().currentUser
        val fileSource = "avatar/" + u!!.uid + ".jpg"
        if(filepath!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child(fileSource)
            imageRef.putFile(filepath)
                .addOnSuccessListener {p0 ->
                    pd.dismiss()
                    //Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainPage::class.java))
                    finish()
                }
                .addOnFailureListener{p0 ->
                    pd.dismiss()
                    startActivity(Intent(this,MainPage::class.java))
                    finish()
                    //Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")

                }
        }
        else{
            startActivity(Intent(this,MainPage::class.java))
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageView2.setImageBitmap(bitmap)

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
