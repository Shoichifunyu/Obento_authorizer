package com.example.myscheduler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.myscheduler.BuildConfig.MONGODB_REALM_APP_ID
import com.example.myscheduler.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.internal.objectstore.OsSyncUser
import io.realm.kotlin.syncSession
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.User
import io.realm.mongodb.auth.EmailPasswordAuth
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.card_layout.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_enroll_comp.*
import kotlinx.android.synthetic.main.fragment_goods.*
import java.net.URL


public class MainActivity : AppCompatActivity(){
    private var user: User? = null

    override fun onStart() {
        super.onStart()
        //ログイン中ユーザの取得
        try {
            user = taskApp.currentUser()

        } catch (e: IllegalStateException) {
            Log.w(TAG(), e)
        }
        //ログイン中ユーザが存在しない時、ログイン画面を表示する
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        //ログイン中ユーザが存在するとき
        else {
            //navigationによる画面遷移を行う。
            val naviController = findNavController(R.id.nav_host_fragment)
            naviController.navigateUp()
        }
    }

    //activity_mainの画面をベースとする上で必要な関数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //logoutメニューをMainActivity上に設置
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
    }

    //logoutメニューを押したときの処理(ログアウト)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                user?.logOutAsync {
                    if (it.isSuccess) {
                        // always close the realm when finished interacting to free up resources
                //        realm.close()
                        user = null
                        Log.v(TAG(), "user logged out")
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Log.e(TAG(), "log out failed! Error: ${it.error}")
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
//RecyclerViewを初期設定して呼び出し、ID順で表示
//private fun setUpRecyclerView(realm: Realm) {
    // a recyclerview requires an adapter, which feeds it items to display.
    // Realm provides RealmRecyclerViewAdapter, which you can extend to customize for your application
    // pass the adapter a collection of Tasks from the realm
    // sort this collection so that the displayed order of Tasks remains stable across updates
//    adapter = TaskAdapter(realm.where<Task>().sort("_id").findAll())
//    recyclerView.layoutManager = LinearLayoutManager(this)
//    recyclerView.adapter = adapter
//    recyclerView.setHasFixedSize(true)
//    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//}
   // companion object {
   //     lateinit var auth_enroll_fab: FloatingActionButton
  //  }
}