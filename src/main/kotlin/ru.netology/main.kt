package ru.netology

data class Audio(
    val id: Int,
    val ownerID: Int,
    val artist: String,
    val title: String,
    val duration: Int? = null,
    val url: String? = null,
    val lyricsID: Int? = null,
    val albumID: Int? = null,
    val genreID: Int? = null,
    val date: Int? = null,
    val noSearch: Boolean? = null,
    val isHQ: Boolean = false,
)

data class Video(
    val vid: Int,
    val ownerID: Int,
    val title: String,
    val description: String? = null,
    val duration: String? = null,
    val link: String? = null,
    val image: String? = null,
    val imageMedium: String? = null,
    val date: Int? = null,
    val player: String? = null,
)

data class Photo(
    val id: Int,
    val albumID: Int? = null,
    val ownerID: Int,
    val userID: Int,
    val text: String? = null,
    val date: Int? = null,
    val width: Int,
    val height: Int
)

data class Link(
    val url: String,
    val title: String,
    val caption: String,
    val description: String? = null,
    val photo: Photo? = null,
    val product: Any? = null,
    val button: Any? = null,
    val previewPage: String? = null,
    val previewURL: String? = null,
)

data class Note(
    val id: Int,
    val ownerID: Int,
    val title: String,
    val text: String,
    val date: Int? = null,
    val comment: Int? = null,
    val readComments: Int? = null,
    val viewURL: String? = null,
)

sealed class Attachment(
    open val type: String
) {
    data class AttachmentAudio(
        val audio: Audio
    ) : Attachment("audio")

    data class AttachmentVideo(
        val video: Video
    ) : Attachment("video")

    data class AttachmentPhoto(
        val photo: Photo
    ) : Attachment("photo")

    data class AttachmentLink(
        val link: Link
    ) : Attachment("link")

    data class AttachmentNote(
        val note: Note
    ) : Attachment("note")
}

data class Post(
    val postID: Long,
    val ownerID: Long = 0,
    val fromID: Long = 0,
    val createdBy: Long? = null,
    val date: Long,
    val text: String,
    val replyOwnerID: Long? = null,
    val comments: Comments? = null,
    val attachments: Array<Attachment> = emptyArray(),
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