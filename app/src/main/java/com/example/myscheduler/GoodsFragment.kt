package com.example.myscheduler


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myscheduler.databinding.FragmentGoodsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.card_layout.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_goods.*
import kotlinx.android.synthetic.main.gds_card_layout.*


internal class GoodsFragment : Fragment() {

    private var user: User? = taskApp.currentUser()
    private val partitionValue: String = "via_android_studio"

    //Realmとの同期設定
    private val config = SyncConfiguration.Builder(user!!, partitionValue)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .schemaVersion(1)
        .build()
    private val realm: Realm = Realm.getInstance(config)

    private var _binding: FragmentGoodsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Realm DBとの同期
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            //成功時
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                //同期したRealmインスタンスを親クラスEnrollCompFragmentのインスタンスに設定
                val realm = this@GoodsFragment.realm

                binding.goods.layoutManager = LinearLayoutManager(context)
                val goods = realm.where<Goods>().findAll()
                //adapterにGoodAdapterを適用し、goodsを代入し、getGoods関数によって処理する。
                //val adapter = GoodAdapter(goods, context, getGoods(resources))
                val adapter = GoodAdapter(goods, context, getShops(resources))
                binding.goods.adapter = adapter
                adapter.setOnButtonClickListener { id ->
                    id.let {
                        val action = GoodsFragmentDirections.actionToEnrollAuth(it)
                        //すでに、auth_update_btn.setOnClickListenerはadapterで設定しているので不要
                        //auth_update_btn.setOnClickListener {
                        findNavController().navigate(action)
                        //}
                    }
                }

                auth_goods_btn.setOnClickListener {
                    val action2 = GoodsFragmentDirections.actionToEnrollAuth()
                    findNavController().navigate(action2)
                }

                booking_list_btn.setOnClickListener {
                    val action3 = GoodsFragmentDirections.actionToEnroll()
                    findNavController().navigate(action3)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



