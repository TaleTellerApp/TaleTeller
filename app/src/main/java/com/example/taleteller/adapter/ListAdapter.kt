package com.example.taleteller.adapter


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taleteller.R
import com.example.taleteller.base.BaseRecyclerViewAdapter
import com.example.taleteller.model.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_details.*

import kotlinx.android.synthetic.main.list_item.view.*
import java.io.File
import java.io.IOException

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
        private val likesView: TextView = view.likes


        init {
            view.setOnClickListener(this)
        }

        fun setUpView(user: User?) {

            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl("gs://taleteller-3cc8c.appspot.com/cover").child(user?.id+".jpg")
            try{
                val file = File.createTempFile("image","jpg")
                storageReference.getFile(file).addOnSuccessListener {

                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    imageView.setImageBitmap(bitmap)

                }.addOnFailureListener{
                    user?.resId?.let { imageView.setImageResource(it) }
                }
            }catch(e : IOException) {

            }

            titleView.text = user?.title
            shortcutView.text = user?.shortcut
            likesView.text = user?.likes

        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(adapterPosition, v)
        }
    }
}