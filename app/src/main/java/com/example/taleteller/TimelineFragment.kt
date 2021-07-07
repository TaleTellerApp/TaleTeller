package com.example.taleteller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.taleteller.Search.Companion.idVar
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_edit_tale.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.android.synthetic.main.fragment_timeline.button2
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class TimelineFragment : Fragment() {
    lateinit var filepath : Uri
    lateinit var filepathaudio : Uri
    var addtitle : EditText? =null
    var addshortcut : EditText? =null
    var addcontent : EditText? =null
    val options = arrayOf("Horror","Comic","Fantasy","Crime")
    var categoryTale : String = ""
    var flagImage = false
    var flagAudio = false

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

        }

        button2.setOnClickListener{
            startFileChooser()
        }
        addRec.setOnClickListener{
            startFileChooserAudio()
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
            db.collection("Tales").whereEqualTo("Content",addContent!!.text.toString()).addSnapshotListener{ snap, e ->
                if(e != null){
                    return@addSnapshotListener
                }
                snap!!.documentChanges.iterator().forEach {doc ->
                    if(doc.type == DocumentChange.Type.ADDED){
                        val id = doc.document.id
                        if(flagAudio){
                            uploadFileAudio(id)
                        }
                        if(flagImage){
                            uploadFile(id)
                        }
                        else{
                            val intent = Intent(activity, MainPage::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }



                    }
                }
            }
        }

    }

    fun startFileChooser() {
        flagImage = true
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }
    fun uploadFile(id : String){

        val fileSource = "cover/"+ id +".jpg"
        val intent = Intent(activity, MainPage::class.java)
        if(filepath!=null) {
            val imageRef = FirebaseStorage.getInstance().reference.child(fileSource)
            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    startActivity(intent)
                    activity?.finish()
                }
                .addOnFailureListener {
                    startActivity(intent)
                    activity?.finish()
                }
        }
        else{
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(getActivity()!!.getContentResolver(),filepath)
            imageCover.setImageBitmap(bitmap)
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
    fun uploadFileAudio(id : String){

        val fileSourceaudio = "rec/"+ id +".mp3"

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
