package com.geras.chat.data

import com.geras.chat.domain.model.Message

fun MessageDTO.toEntity(): Message? {
    return Message(
        userName ?: return null,
        textMessage ?: return null,
        messageTime ?: return null
    )
}