package com.example.taleteller.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taleteller.R
import com.example.taleteller.base.BaseRecyclerViewAdapter
import com.example.taleteller.model.User

import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter: BaseRecyclerViewAdapter<User>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(user = getItem(position))
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val imageView: ImageView = view.image_view
        private val titleView: TextView = view.title
        private val shortcutView: TextView = view.shortcut

        init {
            view.setOnClickListener(this)
        }

        fun setUpView(user: User?) {
            user?.resId?.let { imageView.setImageResource(it) }
            titleView.text = user?.title
            shortcutView.text = user?.shortcut
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(adapterPosition, v)
        }
    }
}