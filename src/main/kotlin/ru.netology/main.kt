package ru.netology

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

class Comments(
    val count: Long = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true,
    val canClose: Boolean = true,
    val canOpen: Boolean = true
)

object WallService{
    private var posts = emptyArray<Post>()

    fun add(post:Post): Post{
        posts += post
        return posts.last()
    }
}