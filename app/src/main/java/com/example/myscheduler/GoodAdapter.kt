package com.example.myscheduler

import android.R.attr
import android.R.attr.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.where
import org.bson.types.ObjectId
import java.io.BufferedInputStream
import java.io.InputStream
import android.R.attr.data
import android.content.Intent.ACTION_OPEN_DOCUMENT


/*
* Scheduleコレクションデータ表示用クラス(Realm取得データをRecyclerViewに表示するためのクラスRealmRecyclerViewAdapterを継承)
* EnrollAdapter: extends the Realm-provided RealmRecyclerViewAdapter to provide data for a RecyclerView to display
* Realm objects on screen to a user.
*/
internal class GoodAdapter(
    data: OrderedRealmCollection<Goods>,
    private val context: Context?,
    private val shops: List<Shop>
    //private val GoodsInfo: MutableList<GoodsInfo>,
):
    RealmRecyclerViewAdapter<Goods,GoodAdapter.ViewHolder>(data,true) {

    //goodIdを受け取るために、引数がObjectId型（NULL許容）で戻り値がない関数型の変数listenerを定義
    private var listener: ((ObjectId?) -> Unit)? = null
    //private var listener: ((Long?) -> Unit)? = null

    //今回は、cardviewではなく、buttonを押して反応するようにしたいので、名前にもbuttonをはっきりと明記
    fun setOnButtonClickListener(listener: (ObjectId?) -> Unit) {
        //fun setOnItemClickListener(listener: (ObjectId?) -> Unit) {
        //fun setOnItemClickListener(listener: (Long?) -> Unit) {
        this.listener = listener
    }
    //ObjectIdで固有のID指定しているのでsetHasStableIdsメソッドは不要

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var goods_nm: TextView = view.findViewById(R.id.goods_name_text_view)
        val shop_nm: TextView = view.findViewById(R.id.shop_name_text_view)

        val good_image: ImageView = view.findViewById(R.id.GoodsImage)

        //var data : Goods? = null
        val auth_udt_btn: FloatingActionButton = view.findViewById(R.id.auth_update_btn)
        val auth_del_btn: FloatingActionButton = view.findViewById(R.id.auth_delete_btn)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoodAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gds_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, goods_pos: Int) {
        //val goods = realm.where<Goods>().findAll().sort("_id")
        val goods: Goods? = getItem(goods_pos)
        if (goods != null) {
            //holder.data = goods
            holder.shop_nm.text = shops[0].shop_name
            //holder.shop_nm.text = GoodsInfo[goods_pos].shop_name
            holder.goods_nm.text = goods.goods_name

            //方法①　Android用画像読み込みライブラリの使用
            //val UURL = goods.goodURL
            //if (context != null) {
            //    Glide.with(context)
            //        .load(UURL)
            //        .centerCrop()
            //        .into(holder.good_image)
            //}

            //方法②　Uri→InputStream→Bitmap
            // 事前にシステムからそのファイルに付与されるURI権限を永続的に維持する。
            //val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
            //    it.addCategory(Intent.CATEGORY_OPENABLE)
            //    it.type = "image/*"
            //   it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            //   it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //}

            //val action: String
            //action = Intent.ACTION_OPEN_DOCUMENT
            val goodsUri: Uri = goods?.goodURL.toUri()
            //var takeFlags: String = Intent.ACTION_OPEN_DOCUMENT
            //takeFlags =
            //    takeFlags + (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            /*val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
                it.addCategory(Intent.CATEGORY_OPENABLE)
                it.type = "image/*"
                it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                it.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
                 */
                 */
            //エラー「No persistable permission grants found for UID 10134」への対策
            /*val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                intent = Intent(Intent.ACTION_GET_CONTENT)
            }
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/*"
            */
            */

            //ファイルを開く（android studioの公式ページを参考にした）
            //val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            //    addCategory(Intent.CATEGORY_OPENABLE)
            //    type = "image/*"
            //}
            //var takeFlags: Int = intent.flags

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //    if (context != null) {

            //context?.contentResolver?から、context?.applicationContext?.contentResolver?に変更
            val takeFlags: Int  =Intent.FLAG_GRANT_READ_URI_PERMISSION or
                          Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    //context?.applicationContext?.contentResolver?.takePersistableUriPermission(goodsUri, takeFlags)
                    //↑元に戻した
            //↑そもそもtakePersistableUriPermission使うとうまく起動しないのでgrantUriPermissionにした。
            context?.grantUriPermission("com.example.myscheduler", goodsUri, takeFlags)
                        //Uri→InputStream→Bitmap
            val stream: InputStream? = context!!.contentResolver?.openInputStream(goodsUri)
            val bitmap = BitmapFactory.decodeStream(BufferedInputStream(stream))
            holder.good_image.setImageBitmap(bitmap)
                //    }
              //  }
                //編集ボタンと削除ボタン、押される可能性は2通り考えられる
                holder.auth_udt_btn.setOnClickListener {
                    listener?.invoke(goods?._id)
                }

                //削除ボタンの場合は、該当のID（のレコード）を削除（Realmのデータベースから）
                //念のため確認ダイアログを表示する
                holder.auth_del_btn.setOnClickListener {
                    //val dialog = ConfirmDialog("削除しますか","削除",
                    //{
                    realm.executeTransaction { db: Realm ->
                        db.where<Goods>().equalTo("_id", goods?._id)
                            ?.findFirst()
                            ?.deleteFromRealm()
                    }
                }

                //},
                //"キャンセル",
                //{
                //     Snackbar.make(it, "キャンセルしました", Snackbar.LENGTH_SHORT).show()
                //  })
                //dialog.show()
                //}
            context?.revokeUriPermission(goodsUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            }
        }
    }

            // Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
            //      .setAction("戻る") { findNavController().popBackStack() }
            //      .setActionTextColor(Color.YELLOW)
            //      .show()

        //else {
            // make sure Glide doesn't load anything into this view until told otherwise
           // if (context != null) {
           //     Glide.with(context).clear(holder.good_image)
           // }
            // remove the placeholder (optional); read comments below
            //holder.good_image.setImageDrawable(null)
        //}


    //override fun getItemCount(position3: Int): Int =
    //     Realm.getInstance(config).use { realm ->
    //          return realm.where<Schedule>().equalTo("_id", getItem(position3)?._id).count()
    //return sum(realm.where<Schedule>.findAll())
    //
    //   }





