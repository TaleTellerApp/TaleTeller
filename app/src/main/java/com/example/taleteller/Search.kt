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
    }

    override fun onCreate() {
        super.onCreate()
    }
}