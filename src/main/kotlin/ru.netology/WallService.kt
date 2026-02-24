package ru.netology

object WallService{
    private var posts = emptyArray<Post>()
    private var uniqueID: Long = 1

    fun add(post:Post): Post{
        posts += post.copy(postID = uniqueID)
        uniqueID++
        return posts.last()
    }

    fun update(post:Post): Boolean {
        for ((index) in posts.withIndex()){
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