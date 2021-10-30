package com.example.myscheduler

import android.R
import android.R.attr
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myscheduler.databinding.FragmentEnrollAuthBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

import kotlinx.android.synthetic.main.fragment_enroll_auth.*
import java.io.BufferedInputStream
import android.content.ContentResolver

import android.R.attr.data
import android.R.attr.grantUriPermissions
import android.content.Context
import android.content.Intent.*
import kotlinx.android.synthetic.main.fragment_goods.*
import org.bson.types.ObjectId
import java.text.Collator.getInstance
import java.util.Calendar.getInstance


internal class EnrollAuthFragment : Fragment() {
    private var _binding: FragmentEnrollAuthBinding? = null
    private val binding get() = _binding!!
    private var user: User? = taskApp.currentUser()
    private val partitionValue: String = "via_android_studio"
    private val config = SyncConfiguration.Builder(user!!, partitionValue)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .schemaVersion(1)
        .build()

    private val realm: Realm = Realm.getInstance(config)
    //private val startForResult = registerForActivityResult(ActivityResultContracts.GetContent(), this::saveEnroll)

    //startActivityForResult実行時に第二引数に代入する
    private val READ_REQUEST_CODE: Int = 42

    private val startForResult = registerForActivityResult(ActivityResultContracts.GetContent())
    {uri: Uri ->
        // 事前にシステムからそのファイルに付与されるURI権限を永続的に維持する。
        /*val intent = Intent(ACTION_OPEN_DOCUMENT).also {
            it.addCategory(CATEGORY_OPENABLE)
            it.type = "image/*"
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        var takeFlags: Int  = (intent.flags
                and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION))
        context?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
         */
         */
        //エラー「Requested flags 0x43, but only 0x3 are allowed」への対策として考えられるもの
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //context?.contentResolver?から、context?.applicationContext?.contentResolver?に変更
        //↑元に戻した
            val takeFlags: Int  = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
            //context?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
        //↑そもそもtakePersistableUriPermission使うとうまく起動しないのでgrantUriPermissionにした。
        context?.grantUriPermission("com.example.myscheduler",uri, takeFlags)
        //    }
        return@registerForActivityResult saveEnroll(uri)
    }

    //GoodsFragmentから受け取った「GoodId」の有無を判断し、データの新規登録か更新かを判断する。
    private val args: EnrollAuthFragmentArgs by navArgs()

    //private val receiveForResult = registerForActivityResult(ActivityResultContracts.) { uri ->
    //    Log.d("EnrollAuthFragment", "geturi: $uri")
    //}
    //    Realm.getInstanceAsync(config, object : Realm.Callback() {
    //        override fun onSuccess(realm: Realm) {
     //           if (args.goodId != null) {
    //                val goods = realm.where<Goods>()
      //                  .equalTo("_id", args.goodId).findFirst()
     //               binding.goodName.setText(goods?.goods_name)
                    //if (URL != null) {
                    //    goods?.goodURL = URL
                    //}
     //           }

  //  }
    //val intent = Intent()
    //private val REQUEST_PERMISSION : Int = 10
    //private val READ_REQUEST_CODE = 42

    //private var uri: Uri? = null

    //val startForResult = registerForActivityResult(
        // ActivityResultContracts.StartActivityForResult())
    //    ActivityResultContracts.StartActivityForResult()
    //)
    //val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
   // { result: ActivityResult? ->
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        //if (uri?.resultCode == Activity.RESULT_OK) {

        //The document selected by the user won't be returned in the intent.
        //Instead, a URI to that document will be contained in the return intent
        // provided to this method as a parameter.
        // Pull that URI using resultData.getData().
      //  if (result != null) {
      //      if (result.data != null) {
       //         var pfDescriptor: ParcelFileDescriptor? = null
      //          try {
      //              var uri: String = result.data.toString()

     //               println("koko?:"+uri)
                    //              // Uriを表示
     //               textView?.setText(
     //                   java.lang.String.format(
     //                       Locale.US,
      //                      "Uri:　%s",
     //                       uri.toString()
     //                   )
     //               )
                    //pfDescriptor = uri?.let {
                   //     context?.getContentResolver()
                   //         ?.openFileDescriptor(it, "r")
                    //}
                   // if (pfDescriptor != null) {
                   //    val fileDescriptor: FileDescriptor =
                   //              pfDescriptor.fileDescriptor
                   //  val bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                  //    pfDescriptor.close()
                  //    goodView?.setImageBitmap(bmp)
                  //   }
       //             } catch (e: IOException) {
        //                e.printStackTrace()
        //            } finally {
        //               try {
         //                 pfDescriptor?.close()
         //       } catch (e: Exception) {
         //           e.printStackTrace()
        //              }
        //        }
       //     }
     //   }
    //}


    //override fun onCreate(savedInstanceState: Bundle?) {
   //     super.onCreate(savedInstanceState)
       // getUri = ActivityResultRegistry.register("key", owner,
        //    ActivityResultContracts.GetContent()
       // ) { uri ->

   // }

    //textView = findViewById(R.id.text_View)
    //imageView = findViewById(R.id.goodImage)

    //schema versionの整合、権限設定
    //val config = RealmConfiguration.Builder()
    //.name("Schedule.realm")
    //          .name("via_android_studio")
    //         .deleteRealmIfMigrationNeeded()
    //         .allowWritesOnUiThread(true)
    //        .allowQueriesOnUiThread(true)
    //      .schemaVersion(1)
    //        .build()
    // realm = Realm.getInstance(config)

    //}

    //try {
    //     user = taskApp.currentUser()
    //} catch (e: IllegalStateException) {
    //    Log.w(TAG(), e)
    //}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnrollAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ファイルを開く（android studioの公式ページを参考にした）
        val intent = Intent(ACTION_OPEN_DOCUMENT).apply {
            addCategory(CATEGORY_OPENABLE)
            //addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            //addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)
            type = "image/*"
        }
        /*val intent = Intent(ACTION_OPEN_DOCUMENT).also {
            it.addCategory(CATEGORY_OPENABLE)
            it.type = "image/*"
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
         */
         */
        //エラー「No persistable permission grants found for UID 10134」への対策
        //val intent: Intent
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //    intent = Intent(ACTION_OPEN_DOCUMENT)
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            //intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        //} else {
       //     intent = Intent(Intent.ACTION_GET_CONTENT)
       // }
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        //intent.addCategory(CATEGORY_OPENABLE)
        //intent.setType("image/*")
        // textView = view.findViewById(R.id.text_View
        //documentDir = File(requireContext().filesDir.toString() + "/document")
        //shareFile = File(documentDir.toString() + "/share_file")
        // var uri = FileProvider.getUriForFile(
        //     requireContext(),
        //     BuildConfig.APPLICATION_ID + ".fileprovider",
        //      shareFile
        //  )
        //val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        // val cursor: Cursor? = requireContext().contentResolver.query(uri!!, projection, null, null, null)
        // if (cursor != null) {
        //     var uri: String? = null
        //    if (cursor.moveToFirst()) {
        //         uri = cursor.getString(0)
        //     }
        //      cursor.close()
        //  }
        //println(uri)


        //goodView = view.findViewById(R.id.goodImage)
        //val getContent = registerForActivityResult(ActivityResultContracts.GetContent(), this::onContent)
        //val startForResult =
        //    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
        //        if (result?.resultCode == Activity.RESULT_OK) {
        //            result.data?.let { data: Intent ->
        //                val uri: Uri? = result.data!!.getData()
        //textView!!.text =
        //    java.lang.String.format(Locale.US, "Uri:　%s", uri.toString())
        //                 println("uri:" + uri)
        //                 return@let uri
        //goodView.setImageURI(uri ?: return@let)
        //Toast.makeText(context, "$uri", Toast.LENGTH_LONG).show()
        //             }
        //         }
        //     }

        //val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        //    if (it) {
        //        saveEnroll(view, return@registerForActivityResult)
        //     }
        // }
        //println("startForResult:"+startForResult)
        //startForResult.apply {
        //    println("startForResult:" + result.data)
        // }
        //   goodView.setImageURI(uri).toString()
        //
        //val URL = textView?.setText(
        //    String.format(Locale.US, "Uri:　%s",intent.data.toString()));
        //println("URL:"+URL)

        //binding.upload.setOnClickListener {
            //val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            //    type = "image/*"
            //    addCategory(Intent.CATEGORY_OPENABLE)
           // }
            //println("nani:" + startForResult.launch("image/*"))
            //startForResult.launch("image/*")
            //var uri = receiveForResult.launch(WRITE_EXTERNAL_STORAGE)
            // Handle the returned Uri
            //println("nani:" + onContent())

            //registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->


                //val photoUri: Uri? = intent.data
                //var cursor: Cursor? = null
                //var filePath: String? = ""



                Realm.getInstanceAsync(config, object : Realm.Callback() {
                    override fun onSuccess(realm: Realm) {
                        if (args.goodId != null) {
                            println("not null")
                            //idフィールドがgoodIdと同じレコードを取得して変数goodsに代入する。
                            val goods = realm.where<Goods>()
                                .equalTo("_id", args.goodId).findFirst()
                            binding.goodName.setText(goods?.goods_name)
                            binding.goodTxtURL.text = goods?.goodURL.toString()
                            //画像を表示させる方法
                            //その①
                            //context?.let {
                           //     Glide.with(it)
                           //         .load(goods?.goodURL)
                           //         .centerCrop()
                           //         .into(binding.goodImage)
                           // }

                            //方法②　Uri→InputStream→Bitmap
                            //事前にシステムからそのファイルに付与されるURI権限を永続的に維持する。
                            //val contentResolver = context!!.applicationContext.contentResolver
                            //val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                           //         Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            //goods?.goodURL?.toUri()?.let {
                           //     contentResolver.takePersistableUriPermission(
                           //         it, takeFlags)
                           // }

                            //Uri→InputStream→Bitmap
                            val stream = goods?.goodURL?.let {
                                context!!.contentResolver.openInputStream(
                                    it.toUri())
                            }
                            val bitmap = BitmapFactory.decodeStream(BufferedInputStream(stream))
                            binding.goodImage.setImageBitmap(bitmap)

                        }


                            binding.authSave.setOnClickListener {
                                //val dialog = ConfirmDialog("画像をアップロード",
                                //"OK",
                                //setFragmentResultListener("requestKey") { key, bundle ->
                                //    val uri = bundle.getString("resultKey")
                                //    println("geturi:" + uri)
                                /*val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                                intent.addCategory(Intent.CATEGORY_OPENABLE)
                                intent.type = "image/*"
                                startForResult.launch(intent.toString())
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                 */*/
                                startForResult.launch(intent.toString())


                                                    //    setFragmentResult(
                                                    //        "URL",
                                                    //        bundleOf("URL" to startForResult.launch(intent)))
                                                    //     setFragmentResultListener("URL") { key, bundle ->
                                                    //    val URL = bundle.getString("URL")
                                                    //    launcher.launch(WRITE_EXTERNAL_STORAGE)
                                             /*   }
                                            }
                                        },
                                            "キャンセル",
                                            {
                                                //     Snackbar.make(it, "キャンセルしました。", Snackbar.LENGTH_SHORT)
                                                //         .show()
                                            })
                                              */



                                    // setFragmentResultListener("URL") { key, bundle ->
                                    //    val URL = bundle.getString("URL")
                                    //     println("URL:$URL")
                                //dialog.show(parentFragmentManager, "save_dialog")
                               // }
                         //   }

                            }
                    }
            })
         }


        //引数にURIを使用（registerForActivityResultの返り値）
        private fun saveEnroll(uri: Uri) {
            when (args.goodId) {
                //goodIdの型は、ObjectIdのため、初期値はNULL
                    // つまり、新規登録の場合
                null -> {
                    realm.executeTransaction { transactionRealm ->
                        val maxId = realm.where<Goods>().max("_id").toString()
                        //countだとIDが削除されたときに対応できないのでmaxにする
                        //val maxId = realm.where<Goods>().max("_id")
                        println(maxId)
                        val nextId = ((maxId).toLong() ?: 0L) + 1L
                        val goods = realm.createObject<Goods>(nextId)
                        goods.goods_name = binding.goodName.text.toString()
                        //setFragmentResultListener("URL") { key, bundle ->
                        //    val UR = bundle.getString("URL")
                        //    println("URL:" + URL)
                        //println("URL:" + URL)

                        //URIの活用方法
                        //MediaScannerに登録を依頼する（2通り）
                        //方法①　画像の登録をMediaScannerに依頼する方法
                        //MediaScannerConnection.scanFile(this,
                        //    URL, null,
                        //    new OnScanCompletedListener() {
                        //        @Override
                        //        public void onScanCompleted(String path, Uri uri) {
                        //            Log.v("MediaScanWork", "file " + path
                        //                     + " was scanned seccessfully: " + uri);
                        //        }
                        //    });

                        //方法②　Intent.ACTION_MEDIA_SCANNER_SCAN_FILEを使う方法
                        //val mediaScanIntent =
                        //    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, URL)
                        //requireContext().sendBroadcast(mediaScanIntent)

                        //方法③　Uri→InputStream→Bitmap
                        // 事前にシステムからそのファイルに付与されるURI権限を永続的に維持する。
                        //val contentResolver = context?.applicationContext?.contentResolver
                        //val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        //        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        //if (URL != null) {
                        //    if (contentResolver != null) {
                        //        contentResolver.takePersistableUriPermission(URL, takeFlags)
                        //    }
                       // }
                        goods.goodURL = uri.toString()
                        println("URL:" + uri)
                        context?.revokeUriPermission(uri,
                            FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    //Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                    //    .setAction("戻る") { findNavController().popBackStack() }
                    //    .setActionTextColor(Color.YELLOW)
                    //   .show()
                }
                //更新の場合
                else -> {
                    realm.executeTransaction { db: Realm ->
                        //findFirstメソッドを使用して、更新対象を取得する
                       val goods =
                            db.where<Goods>().equalTo("_id", args.goodId).findFirst()
                        goods?.goods_name = binding.goodName.text.toString()
                        //setFragmentResultListener("URL") { key, bundle ->
                        //     val URL = bundle.getString("URL")
                        //    println("URL:" + URL)
                        goods?.goodURL = uri.toString()
                      println("URL:" + uri)
                        context?.revokeUriPermission(uri,
                            FLAG_GRANT_READ_URI_PERMISSION)

                    //MediaScannerに登録を依頼する（2通り）
                    //方法①　画像の登録をMediaScannerに依頼する方法
                    //MediaScannerConnection.scanFile(this,
                    //    URL, null,
                    //    new OnScanCompletedListener() {
                    //        @Override
                    //        public void onScanCompleted(String path, Uri uri) {
                    //            Log.v("MediaScanWork", "file " + path
                    //                     + " was scanned seccessfully: " + uri);
                    //        }
                    //    });

                    //方法②　Intent.ACTION_MEDIA_SCANNER_SCAN_FILEを使う方法
                    //val mediaScanIntent =
                    //    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, URL)
                    //requireContext().sendBroadcast(mediaScanIntent)
                    }
                   // Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                  //      .setAction("戻る") { findNavController().popBackStack() }
                  //      .setActionTextColor(Color.YELLOW)
                  //      .show()
                }
            }
}


        //private fun onContent(uri: Uri?): Uri? {
        //    println(uri)
        //    return uri
       // }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        override fun onDestroy() {
            super.onDestroy()
            realm.close()
        }
    }

