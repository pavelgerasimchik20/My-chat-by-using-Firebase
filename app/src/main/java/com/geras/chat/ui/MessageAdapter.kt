package com.geras.chat.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geras.chat.R
import com.geras.chat.data.MessageDTO
import com.geras.chat.domain.model.Message

class MessageAdapter :
    RecyclerView.Adapter<MessageViewHolder>() {

    var messages = mutableListOf<Message>()

    fun updateMessages(list: List<Message>) {
        messages.clear()
        messages.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.list_item, parent, false)
        return MessageViewHolder(item)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messages[position]
        holder.bind(item)
    }

    override fun getItemCount() = messages.size
}

class MessageViewHolder(item: View) :
    RecyclerView.ViewHolder(item) {

    private val userName: TextView = item.findViewById(R.id.user_name)
    private val messageTime: TextView = item.findViewById(R.id.message_time)
    private val messageText: TextView = item.findViewById(R.id.message_text)

    fun bind(message: Message) {
        userName.text = message.userName
        messageTime.text = message.messageTime.toString()
        messageText.text = message.textMessage
    }
}