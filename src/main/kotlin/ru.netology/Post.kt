package ru.netology

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Post(
    val postID: Long,
    val ownerID: Long,
    val fromID: Long,
    val createdBy: Long,
    val date: Long,
    val text: String,
    val replyOwnerID: Long,
    val comments: Comments,
    val canPin: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean
)
