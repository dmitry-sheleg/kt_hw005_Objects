package ru.netology

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WallServiceTest {
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add() {

        val add = Post(
            postID = 0,
            date = 20260224145050,
            text = "First post",
            comments = Comments()
        )

        val result = (WallService.add(add).postID != 0L)

        assertTrue(result)
    }

    @Test
    fun updateExisting() {

        WallService.add(
            Post(
                postID = 0,
                date = 20260224145050,
                text = "First post",
                comments = Comments()
            )
        )

        WallService.add(
            Post(
                postID = 0,
                date = 20260224155050,
                text = "Next post",
                comments = Comments()
            )
        )

        WallService.add(
            Post(
                postID = 0,
                date = 20260224165050,
                text = "Another post",
                comments = Comments()
            )
        )

        val update = Post(
            postID = 3,
            date = 20260224175050,
            text = "Too post",
            comments = Comments()
        )

        val result = WallService.update(update)

        assertTrue(result)
    }

    @Test
    fun updateNonExisting() {

        WallService.add(
            Post(
                postID = 0,
                date = 20260224145050,
                text = "First post",
                comments = Comments()
            )
        )

        WallService.add(
            Post(
                postID = 0,
                date = 20260224155050,
                text = "Next post",
                comments = Comments()
            )
        )

        WallService.add(
            Post(
                postID = 0,
                date = 20260224165050,
                text = "Another post",
                comments = Comments()
            )
        )

        val update = Post(
            postID = 10,
            date = 20260224175050,
            text = "Too post",
            comments = Comments()
        )

        val result = WallService.update(update)

        assertFalse(result)
    }

    @Test
    fun shouldAddCommentToExistingPost() {
        // Arrange

        val post = Post(
            postID = 1,
            date = 20260224145050,
            text = "Какой-то пост",
            comments = Comments()
        )

        WallService.add(post)

        val comment = Comment(
            fromId = 555,
            postId = 0,
            text = "Тестовый комментарий"
        )

        // Act
        val createdComment = WallService.createComment(post.postID, comment)

        // Assert: проверяем результаты
        // Проверяем, что комментарий был добавлен в массив comments
        assertTrue(WallService.getComments().contains(createdComment))

        // Проверяем, что ID поста в комментарии соответствует ожидаемому
        assertEquals(post.postID, createdComment.postId)

        // Проверяем текст комментария
        assertEquals("Тестовый комментарий", createdComment.text)

        // Проверяем, что автор комментария указан верно
        assertEquals(555, createdComment.fromId)
    }

    @Test (expected = PostNotFoundException::class)
    fun shouldThrowPostNotFoundException() {
        // Arrange: не добавляем никаких постов

        val comment = Comment(
            fromId = 555,
            postId = 0,
            text = "Комментарий к несуществующему посту"
        )

        // Act: пытаемся добавить комментарий к посту с ID=666 (которого нет)
        WallService.createComment(666, comment)
    }

}

