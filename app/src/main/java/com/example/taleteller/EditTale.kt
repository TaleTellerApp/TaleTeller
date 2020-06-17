package com.example.taleteller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.taleteller.DetailsActivity.Companion.EXTRA_USER_MODEL
import com.example.taleteller.Search.Companion.idVar
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_tale.*
import kotlinx.android.synthetic.main.activity_edit_tale.button2
import kotlinx.android.synthetic.main.activity_first_login.*

class EditTale : AppCompatActivity() {
    lateinit var filepath : Uri
    lateinit var filepathaudio : Uri
    lateinit var optionCategory: Spinner
    var categorySearch: String = ""
    var flagImage = false
    var flagAudio = false

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
            if(flagAudio){
                uploadFileAudio()
            }
            if(flagImage){
                uploadFile()
            }
            else{
                startActivity(Intent(this,MainPage::class.java))
                finish()
            }





        }

        button2.setOnClickListener{
            startFileChooser()
        }
        chooseRec.setOnClickListener{
            startFileChooserAudio()
        }
    }

    fun startFileChooser() {
        flagImage = true
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }
    fun uploadFile(){
        val u = FirebaseAuth.getInstance().currentUser
        val fileSource = "cover/" + idVar + ".jpg"
        if(filepath!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child(fileSource)
            imageRef.putFile(filepath)
                .addOnSuccessListener {p0 ->
                    pd.dismiss()
                    startActivity(Intent(this,MainPage::class.java))
                    finish()
                    //Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()
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
            imageView.setImageBitmap(bitmap)

        }
        else if (requestCode==112 && resultCode == Activity.RESULT_OK && data != null){
            filepathaudio = data.data!!
        }
    }

    fun startFileChooserAudio() {
        flagAudio = true
        var i = Intent()
        i.setType("audio/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Recording"),112)
    }
    fun uploadFileAudio(){

        val fileSourceaudio = "rec/"+ idVar +".mp3"

        if(filepathaudio!=null) {
            val imageRef = FirebaseStorage.getInstance().reference.child(fileSourceaudio)
            imageRef.putFile(filepathaudio)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }
}
