package com.example.myscheduler

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myscheduler.databinding.FragmentEnrollCompBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.card_layout.view.*

//const val ROW_POSITION = "ROW_POSITION"

internal class EnrollCompFragment() : Fragment(){
    private var _binding: FragmentEnrollCompBinding? = null
    private val binding get() = _binding!!
   // private lateinit var realm: Realm
    private var user: User? = taskApp.currentUser()
    private val partitionValue: String = "via_android_studio"
    private val config = SyncConfiguration.Builder(user!!, partitionValue)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .schemaVersion(1)
        .build()
    private val realm: Realm = Realm.getInstance(config)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnrollCompBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                //同期したRealmインスタンスを親クラスEnrollCompFragmentのインスタンスに設定
                val realm = this@EnrollCompFragment.realm

        binding.enrollComplist.layoutManager = LinearLayoutManager(context)
        val schedules = realm.where<Schedule>().findAll()
        val adapter = EnrollAdapter(schedules)
        binding.enrollComplist.adapter = adapter

        adapter.setOnItemClickListener { id ->
            id?.let {
                val action = EnrollCompFragmentDirections.actionToEnrollscrn(it)
                findNavController().navigate(action)
            }
        }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

      override fun onDestroy() {
           super.onDestroy()
           realm.close()
      }
}