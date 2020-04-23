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
import com.example.taleteller.DetailsActivity
import com.example.taleteller.HomeFragment
import com.example.taleteller.R
import com.example.taleteller.adapter.ListAdapter
import com.example.taleteller.callback.OnItemClickListener
import com.example.taleteller.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*


class ListFragmentOwner: Fragment() {
    var list: ArrayList<User> = ArrayList<User>()
    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListAdapter


    companion object {
        var TAG = ListFragmentOwner::class.java.simpleName
        const val ARG_POSITION: String = "positioin"

        fun newInstance(): ListFragmentOwner {
            var fragment = ListFragmentOwner();
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

    private fun initView(){
        setUpAdapter()
        initializeRecyclerView()
        getMyData()


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

//   private fun setUpDummyData(){
//        var list: ArrayList<User> = ArrayList<User>()
//        list.add(User("Title1", "Shortcut1",R.drawable.user))


    //        adapter.addItems(list)
    //   }

    fun getMyData(){
        val db = FirebaseFirestore.getInstance()
        val u = FirebaseAuth.getInstance().currentUser
        db.collection("Tales").orderBy("Likes",Query.Direction.DESCENDING).whereEqualTo("UserID",u!!.uid).addSnapshotListener{snap , e ->
            if(e != null){

                Log.d("exist", "Current data: ")
                return@addSnapshotListener
            }
            snap!!.documentChanges.iterator().forEach {doc ->
                if(doc.type == DocumentChange.Type.ADDED){
                    val id = doc.document.id
                    Log.d("Current",id )
                    val tit = doc.document.get("Title") as? String
                    val sho = doc.document.get("Shortcut") as? String
                    val con = doc.document.get("Content") as? String
                    val own = doc.document.get("UserID") as? String
                    list.add(User(tit!!,sho!!,con!!,own!!,R.drawable.okladka))


                }

            }
            adapter.addItems(list)

        }


    }


}