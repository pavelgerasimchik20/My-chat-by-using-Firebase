package com.geras.chat.data

import com.google.gson.annotations.SerializedName

data class MessageDTO(
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("textMessage") val textMessage: String? = null,
    @SerializedName("messageTime") val messageTime: String? = null
)