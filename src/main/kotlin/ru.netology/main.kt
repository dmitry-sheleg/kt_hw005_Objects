package ru.netology


data class Post(
    val postID: Long,
    val ownerID: Long = 0L,
    val fromID: Long = 0L,
    val createdBy: Long = 0L,
    val date: Long,
    val text: String,
    val replyOwnerID: Long = 0L,
    val comments: Comments,
    val canPin: Boolean = true,
    val canDelete: Boolean = true,
    val canEdit: Boolean = true
)

class Comments(
    val count: Long = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true,
    val canClose: Boolean = true,
    val canOpen: Boolean = true
)

object WallService {
    private var posts = emptyArray<Post>()
    private var uniqueID: Long = 1

    fun add(post: Post): Post {
        posts += post.copy(postID = uniqueID)
        uniqueID++
        return posts.last()
    }

    fun update(post: Post): Boolean {
        for ((index) in posts.withIndex()) {
            if (posts[index].postID == post.postID) {
                posts[index] = post.copy()
                return true
            }
        }
        return false
    }

    fun clear() {
        posts = emptyArray()
        uniqueID = 1
    }
}