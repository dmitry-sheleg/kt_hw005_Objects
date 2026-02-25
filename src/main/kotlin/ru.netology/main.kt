package ru.netology

import java.util.Objects

interface Attachments{
    val type: String
    val attachment: Objects
}

class Audio(
    val id: Int,
    val ownerID: Int,
    val artist: String,
    val title: String,
    val duration: String,
    val url: String,
    val lyricsID: Int,
    val albumID: Int,
    val genreID: Int,
    val date: Int,
    val noSearch: Boolean?,
    val isHQ: Boolean
)

class Video(
    val vid: Int,
    val ownerID: Int,
    val title: String,
    val description: String,
    val duration: String,
    val link: String,
    val image: String,
    val imageMedium: String,
    val date: Int,
    val player: String
)

class Photo(
    val id: Int,
    val albumID: Int,
    val ownerID: Int,
    val userID: Int,
    val text: String,
    val date: Int,
    val sizes: Array<Sizes>,
    val width: Int,
    val height: Int
)

data class Sizes(val type: String, val url: String, val width: Int, val height: Int)

class Link(
    val url: String,
    val title: String,
    val caption: String,
    val description: String,
    val photo: Objects,
    val product: Objects,
    val button: Objects,
    val previewPage: String,
    val previewURL: String
)

data class Post(
    val postID: Long,
    val ownerID: Long = 0,
    val fromID: Long = 0,
    val createdBy: Long? = null,
    val date: Long,
    val text: String,
    val replyOwnerID: Long? = null,
    val comments: Comments?,
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