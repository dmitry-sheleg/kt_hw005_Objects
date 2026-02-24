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
            ownerID = 123,
            fromID = 234,
            createdBy = 345,
            date = 20260224145050,
            text = "First post",
            replyOwnerID = 456,
            comments = Comments(),
            canPin = true,
            canDelete = true,
            canEdit = true
        )

        // выполняем целевое действие
        val result = (WallService.add(add).postID != 0L)

        // проверяем результат (используйте assertTrue или assertFalse)
        assertTrue(result)
    }

    @Test
    fun updateExisting() {

        WallService.add(
            Post(
                postID = 0,
                ownerID = 123,
                fromID = 234,
                createdBy = 345,
                date = 20260224145050,
                text = "First post",
                replyOwnerID = 456,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        WallService.add(
            Post(
                postID = 0,
                ownerID = 223,
                fromID = 334,
                createdBy = 445,
                date = 20260224155050,
                text = "Next post",
                replyOwnerID = 556,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        WallService.add(
            Post(
                postID = 0,
                ownerID = 323,
                fromID = 434,
                createdBy = 545,
                date = 20260224165050,
                text = "Another post",
                replyOwnerID = 656,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        val update = Post(
            postID = 3,
            ownerID = 323,
            fromID = 434,
            createdBy = 545,
            date = 20260224175050,
            text = "Too post",
            replyOwnerID = 656,
            comments = Comments(),
            canPin = true,
            canDelete = true,
            canEdit = true
        )

        // выполняем целевое действие
        val result = WallService.update(update)

        // проверяем результат (используйте assertTrue или assertFalse)
        assertTrue(result)
    }

    @Test
    fun updateNonExisting() {

        WallService.add(
            Post(
                postID = 0,
                ownerID = 123,
                fromID = 234,
                createdBy = 345,
                date = 20260224145050,
                text = "First post",
                replyOwnerID = 456,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        WallService.add(
            Post(
                postID = 0,
                ownerID = 223,
                fromID = 334,
                createdBy = 445,
                date = 20260224155050,
                text = "Next post",
                replyOwnerID = 556,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        WallService.add(
            Post(
                postID = 0,
                ownerID = 323,
                fromID = 434,
                createdBy = 545,
                date = 20260224165050,
                text = "Another post",
                replyOwnerID = 656,
                comments = Comments(),
                canPin = true,
                canDelete = true,
                canEdit = true
            )
        )

        val update = Post(
            postID = 10,
            ownerID = 323,
            fromID = 434,
            createdBy = 545,
            date = 20260224175050,
            text = "Too post",
            replyOwnerID = 656,
            comments = Comments(),
            canPin = true,
            canDelete = true,
            canEdit = true
        )

        // выполняем целевое действие
        val result = WallService.update(update)

        // проверяем результат (используйте assertTrue или assertFalse)
        assertFalse(result)
    }

}

