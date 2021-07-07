package com.example.taleteller

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.taleteller.Search.Companion.sentenceCounter
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class DetailsActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    val player = MediaPlayer()
    var playStop = false
    private lateinit var textView: TextView
    private var tts: TextToSpeech? = null
    var text : String = ""


    private lateinit var textToSpeech: TextToSpeech

    var myList: List<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)

        supportActionBar?.title = user.title


        textView = findViewById(R.id.text_view)


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

        userIcon.setOnClickListener{

            prepareAudio()

            backUser.visibility = View.VISIBLE
            userIcon.visibility = View.GONE
            synIcon.visibility = View.GONE
        }

        synIcon.setOnClickListener{

            synPlayStop.visibility = View.VISIBLE
            backSyn.visibility = View.VISIBLE
            synIcon.visibility = View.GONE
            userIcon.visibility = View.GONE
        }

        backUser.setOnClickListener{
            userPlayStop.visibility = View.GONE
            backUser.visibility = View.GONE
            userIcon.visibility = View.VISIBLE
            synIcon.visibility = View.VISIBLE
        }

        backSyn.setOnClickListener{

            synPlayStop.visibility = View.GONE
            backSyn.visibility = View.GONE
            synIcon.visibility = View.VISIBLE
            userIcon.visibility = View.VISIBLE
        }



        tts = TextToSpeech(this, this)


        synPlayStop.setOnClickListener(){
            if(playStop){
                playStop = false
                synPlayStop.setImageResource(R.drawable.ic_action_play)
                pause()
            }
            else{
                synPlayStop.setImageResource(R.drawable.ic_action_stop)
                playStop = true
                resume()
            }
        }


        userPlayStop.setOnClickListener{
            if(playStop){
                player.pause()
                playStop = false
                userPlayStop.setImageResource(R.drawable.ic_action_play)
            }
            else{
                player.start()
                playStop = true
                userPlayStop.setImageResource(R.drawable.ic_action_stop)
            }
        }



    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(utteranceId: String) {
                    if(sentenceCounter < utteranceId.toInt()-1){
                        speakText()
                    }
                    else{
                        sentenceCounter = 1
                        playStop = false
                        synPlayStop.setImageResource(R.drawable.ic_action_play)
                    }
                }
                override fun onError(utteranceId: String) {}
                override fun onStart(utteranceId: String) {}
            })
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
            }
        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }


    fun resume() {
        sentenceCounter -=1
        speakText()
    }
    fun pause() {
        tts?.stop()
    }
    fun speakText() {
        val text = text_view.text
        myList =text.split(".")
        tts!!.speak(myList[sentenceCounter], TextToSpeech.QUEUE_FLUSH, null,myList.size.toString())
        sentenceCounter += 1

    }



    fun prepareAudio(){
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child("rec/"+ user.id.toString() +".mp3").downloadUrl.addOnSuccessListener {
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
                player.setDataSource(it.toString())
                player.prepare()
                userPlayStop.visibility = View.VISIBLE
            } catch (e: IOException) {

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
            editButton.visibility = View.VISIBLE
        }

    }


    fun getData(){
        val user: User = intent.getParcelableExtra(EXTRA_USER_MODEL)
        val db = FirebaseFirestore.getInstance()
        var userId = "a"
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl("gs://taleteller-3cc8c.appspot.com/avatar").child(user.owner+".jpg")
        val storageReferenceCover = storage.getReferenceFromUrl("gs://taleteller-3cc8c.appspot.com/cover").child(user.id+".jpg")
        likeNumber.text = user?.likes
        try{
            val file = File.createTempFile("image","jpg")
            storageReferenceCover.getFile(file).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                image_view.setImageBitmap(bitmap)

            }.addOnFailureListener{
                image_view.setImageResource(user?.resId)
            }
        }catch(e : IOException) {
        }

        try{
            val file = File.createTempFile("image","jpg")
            storageReference.getFile(file).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                detailsUserAvatar.setImageBitmap(bitmap)

            }
        }catch(e : IOException) {
        }



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

        val storageRef = storage.reference
        storageRef.child("rec/"+ user?.id+".mp3").downloadUrl.addOnSuccessListener {
            userIcon.visibility = View.VISIBLE
        }.addOnFailureListener {

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

