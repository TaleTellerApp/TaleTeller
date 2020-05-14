package com.example.taleteller.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taleteller.*
import com.example.taleteller.adapter.ListAdapter
import com.example.taleteller.callback.OnItemClickListener
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*


class ListFragmentDate: Fragment() {
    var list: ArrayList<User> = ArrayList<User>()
    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListAdapter


    companion object {
        var TAG = ListFragmentDate::class.java.simpleName
        const val ARG_POSITION: String = "positioin"

        fun newInstance(): ListFragmentDate {
            var fragment = ListFragmentDate();
            val args = Bundle()
            args.putInt(ARG_POSITION, 1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()

    }

    private fun onCreateComponent() {
        adapter = ListAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        initView()
        return rootView
    }

    fun initView(){
        setUpAdapter()
        initializeRecyclerView()
        getData()
    }

    private fun setUpAdapter() {
        adapter.setOnItemClickListener(onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View?) {
                var user = adapter.getItem(position)
                startActivity(context?.let {ctx ->
                    user?.let {
                            user -> DetailsActivity.newIntent(ctx, user)
                    }
                })
            }
        })
    }

    private fun initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    fun getData(){

        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        db.collection("Tales").orderBy("EditDate",Query.Direction.DESCENDING).addSnapshotListener{snap , e ->
            if(e != null){
                return@addSnapshotListener
                }
            snap!!.documentChanges.iterator().forEach {doc ->
                if(doc.type == DocumentChange.Type.ADDED){
                    val id = doc.document.id
                    val tit = doc.document.get("Title") as? String
                    val sho = doc.document.get("Shortcut") as? String
                    val con = doc.document.get("Content") as? String
                    val own = doc.document.get("UserID") as? String
                    val likes = doc.document.get("Likes").toString() as? String
                    list.add(User(id!!,tit!!,sho!!,con!!,own!!,likes!!,R.drawable.okladka))

                    adapter.notifyDataSetChanged()
                }

            }
            adapter.addItems(list)

        }
    }
}