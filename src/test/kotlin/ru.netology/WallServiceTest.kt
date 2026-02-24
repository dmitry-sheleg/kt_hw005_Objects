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

}

