package com.example.taleteller.callback

import android.view.View

interface OnItemClickListener {
    abstract fun onItemClick(position: Int, view: View?)
}