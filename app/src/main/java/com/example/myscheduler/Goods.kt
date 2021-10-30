package com.example.myscheduler

import android.content.res.Resources
import android.graphics.Insets.add
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.OneShotPreDrawListener.add
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.kotlin.where
import io.realm.log.RealmLog.add
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import java.io.BufferedReader
import java.io.InputStreamReader

private var user: User? = taskApp.currentUser()
private val partitionValue: String = "via_android_studio"
private val config = SyncConfiguration.Builder(user!!, partitionValue)
    .allowWritesOnUiThread(true)
    .allowQueriesOnUiThread(true)
    .schemaVersion(1)
    .build()
// private lateinit var realm: Realm
val realm: Realm = Realm.getInstance(config)

//Goodsクラスの定義作成
open class Goods(project: String? = "via_android_studio"): RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var _partition: String? = project
    @Required
    var goods_name: String = ""
    var goodURL: String = ""
}

/*
data class Shops (
        val shop_name: String
)
data class GoodsInfo(
    var goods_name : String,
    var shop_name: String,
    var goodImage: String = ""
)

fun getGoods(resources: Resources): MutableList<GoodsInfo> {
    val assetManager = resources.assets
    //変数arrayにGoodsテーブルから全件抽出した結果を代入
    var array = realm.where<Goods>().findAll().sort("_id")
    //お店の情報（JSON）を変数に代入し、MutableList化
    val inputStream2 = assetManager.open("shop_info.json")
    val bufferedReader2 = BufferedReader(InputStreamReader(inputStream2))
    val str2: String = bufferedReader2.readText()
    val listType2 = object : TypeToken<MutableList<Shops>>() {}.type
    var array2 = Gson().fromJson<MutableList<Shops>>(str2, listType2)
    //お店の情報とGoods（商品）情報を統合したMutableListを作成
    var array4 = mutableListOf<GoodsInfo>()
    for (i in array.indices) {
        var array3 = array[i]?.let { array[i]?.let { it1 -> GoodsInfo(it.goods_name,array2[0].shop_name, it1.goodURL) } }
        if (array3 != null) {
            array4.add(array3)
        }
        }
    return array4
}
*/

open class User(project: String? = "via_android_studio"): RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var _partition: String? = project
    @Required
    var personname: String = ""
}