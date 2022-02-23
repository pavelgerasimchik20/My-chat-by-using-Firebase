package com.geras.chat.data

import android.widget.EditText
import androidx.core.view.isVisible
import com.geras.chat.databinding.ActivityMainBinding
import com.geras.chat.ui.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class DBManager {

    internal val database: DatabaseReference =
        Firebase.database("https://chat-3ac96-default-rtdb.europe-west1.firebasedatabase.app").reference

    fun launchEventListener(adapter: MessageAdapter, binding: ActivityMainBinding) : ValueEventListener{
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
                    binding.recyclerView.smoothScrollToPosition(adapter.messages.size)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        return postListener
    }

    fun writeToDatabase(editText: EditText){
        database.push().setValue(
            MessageDTO(
                userName = FirebaseAuth.getInstance().currentUser?.email,
                textMessage = editText.text.toString(),
                messageTime = Date().time
            )
        )
    }
}