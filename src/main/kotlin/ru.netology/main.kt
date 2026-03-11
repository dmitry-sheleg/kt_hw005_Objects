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

data class Comment(
    val id: Long = 0,
    val fromId: Long,
    val postId: Long,
    val date: Long = System.currentTimeMillis(),
    val text: String,
    val replyToUser: Long? = null,
    val replyToComment: Long? = null
)

class PostNotFoundException(message: String) : Exception(message)

object WallService {
    private var posts = emptyArray<Post>()
    private var uniqueID: Long = 1
    private var comments = emptyArray<Comment>()

    fun createComment(postId: Long, comment: Comment): Comment {

        var postFound = false

        for (post in posts) {
            if (post.postID == postId) {
                postFound = true
                break
            }
        }

        if (!postFound) {
            throw PostNotFoundException("Пост с указанным ID $postId не найден")
        }

        val newComment = comment.copy(postId = postId)
        comments += newComment
        return newComment
    }

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

    // Метод для доступа к данным (для тестов)
    fun getComments(): Array<Comment> = comments
}