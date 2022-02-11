package com.geras.chat.domain.model

data class Message(

    var userName: String,
    var textMessage: String,
    var messageTime: Long
)