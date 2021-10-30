package com.example.myscheduler

import android.graphics.Color
import android.hardware.camera2.params.ColorSpaceTransform
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myscheduler.databinding.FragmentEnrollBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import android.text.format.DateFormat
import android.util.Log
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.realm.RealmConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.fragment_enroll.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EnrollFragment : Fragment() {
    private var _binding: FragmentEnrollBinding? = null
    private val binding get() = _binding!!
    private var user: User? = taskApp.currentUser()
    private val partitionValue: String = "via_android_studio"
    private val config = SyncConfiguration.Builder(user!!, partitionValue)
    .allowWritesOnUiThread(true)
    .allowQueriesOnUiThread(true)
    .schemaVersion(1)
            .build()


    private val realm: Realm = Realm.getInstance(config)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEnrollBinding.inflate(inflater,container,false)
        return binding.root
    }
    private val args: EnrollFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                if (args.enrollId != null) {

                    val schedule = realm.where<Schedule>()
                            .equalTo("_id", args.enrollId).findFirst()
                    binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", schedule?.date))
                    binding.timeEdit.setText(DateFormat.format("HH:mm", schedule?.date))
                    binding.PersonNameEdit.setText(schedule?.personname)
                    binding.detailEdit.setText(schedule?.detail)
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