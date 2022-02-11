package com.geras.chat.data

import com.google.gson.annotations.SerializedName

data class MessageDTO(

    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("textMessage")
    var textMessage: String? = null,
    @SerializedName("messageTime")
    var messageTime: Long? = null
)