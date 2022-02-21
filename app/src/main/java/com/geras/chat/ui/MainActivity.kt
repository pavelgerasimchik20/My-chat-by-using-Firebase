package com.geras.chat.ui

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.firebase.ui.auth.AuthUI
import com.geras.chat.R
import com.geras.chat.data.MessageDTO
import com.geras.chat.data.toEntity
import com.geras.chat.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    private lateinit var database: DatabaseReference
    private val adapter = MessageAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        //user hasn't authorized
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().build(),
                SIGN_IN_CODE
            )
        }

        val editText = binding.messageField
        binding.recyclerView.adapter = adapter

        database =
            Firebase.database("https://chat-3ac96-default-rtdb.europe-west1.firebasedatabase.app").reference
        binding.progressBar.isVisible = true

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val message = dataSnapshot.getValue<HashMap<String, MessageDTO>>()
                message?.let { it ->
                    val listMessages = it.values.toList().mapNotNull {
                        it.toEntity()
                    }.sortedBy {
                        it.messageTime
                    }
                    adapter.updateMessages(listMessages)
                    binding.progressBar.isVisible = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        database.addValueEventListener(postListener)

        binding.btnSend.setOnClickListener {
            if (editText.text.isNullOrEmpty()) {
                Snackbar.make(binding.activityMain, "Enter the message", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                database.push().setValue(
                    MessageDTO(
                        userName = FirebaseAuth.getInstance().currentUser?.email,
                        textMessage = editText.text.toString(),
                        messageTime = Date().time
                    )
                )
                createNotification(editText.text)
                editText.setText("")
                binding.recyclerView.smoothScrollToPosition(adapter.messages.size)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(message: CharSequence) {
        val notification =
            Notification.Builder(this, "id")
                .setAutoCancel(true)
                .setColor(Color.YELLOW)
                .setSmallIcon(R.drawable.ic_baseline_chat_bubble_24)
                .setContentTitle("New message")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setTimeoutAfter(1500)
                .setPriority(Notification.PRIORITY_HIGH)
                .build()
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1,notification)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
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

    companion object {
        private const val SIGN_IN_CODE = 1
    }
}