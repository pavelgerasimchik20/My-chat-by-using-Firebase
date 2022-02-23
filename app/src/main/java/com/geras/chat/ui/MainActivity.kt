package com.geras.chat.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.firebase.ui.auth.AuthUI
import com.geras.chat.data.DBManager
import com.geras.chat.databinding.ActivityMainBinding
import com.geras.chat.ui.notification.NotificationController
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    private val databaseManager = DBManager()
    private val adapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        MobileAds.initialize(this) {}
        //user hasn't authorized
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().build(),
                1
            )
        }

        val editText = binding.messageField
        binding.recyclerView.adapter = adapter
        binding.progressBar.isVisible = true

        databaseManager.database.addValueEventListener(
            databaseManager.launchEventListener(
                adapter,
                binding
            )
        )

        binding.btnSend.setOnClickListener {
            if (editText.text.isNullOrEmpty()) {
                Snackbar.make(binding.activityMain, "Enter the message", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                databaseManager.writeToDatabase(editText)
                NotificationController.createNotification(this, manager, editText.text)
                editText.setText("")
                binding.recyclerView.smoothScrollToPosition(adapter.messages.size)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(binding.activityMain, "You`re authorized", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                Snackbar.make(binding.activityMain, "You`re not authorized", Snackbar.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }
}