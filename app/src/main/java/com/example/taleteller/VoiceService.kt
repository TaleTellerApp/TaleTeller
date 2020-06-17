package com.example.taleteller

import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi

class VoiceService(t: String) {

    private lateinit var textToSpeech: TextToSpeech
    var text = t
    var sentenceCounter: Int = 1
    var myList: List<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resume() {
        sentenceCounter -= 1
        speakText()
    }

    fun pause() {
        textToSpeech.stop()
    }

    fun stop() {
        sentenceCounter = 0
        textToSpeech.stop()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speakText() {

        var myText = text

        myList =myText.split(".")
        textToSpeech.speak(myList[sentenceCounter], TextToSpeech.QUEUE_FLUSH, null,"")
        sentenceCounter++
        speakText()

    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onDone(p0: String?) {
        speakText()
    }
}