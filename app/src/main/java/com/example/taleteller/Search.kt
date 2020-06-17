package com.example.taleteller

import android.app.Application

class Search: Application() {
    companion object {
        var titleVar = ""
        var categoryVar = "default"
        var orderVar = "Likes"
        var contentVar = ""
        var shortcutVar = ""
        var idVar = ""
        var sentenceCounter = 1
    }

    override fun onCreate() {
        super.onCreate()
    }
}