package ru.netology

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NotesServiceTest {
    private val service = NotesService()

    @Before
    fun clearBeforeTest() {
        service.clear()
    }
    
    @Test
    fun createAndGetNote() {
        // Arrange
        val title = "Test Note"
        val text = "Note content"

        // Act
        val createdNote = service.createNote(title, text)
        val retrievedNote = service.getNote(createdNote.id)

        // Assert
        assertNotNull(retrievedNote)
        assertEquals(title, retrievedNote?.title)
        assertEquals(text, retrievedNote?.text)
    }

    @Test
    fun updateNote() {
        // Arrange
        val note = service.createNote("Old Title", "Old Text")

        // Act
        val updatedNote = service.updateNote(note.id, "New Title", "New Text")

        // Assert
        assertEquals("New Title", updatedNote.title)
        assertEquals("New Text", updatedNote.text)
    }

    @Test
    fun deleteAndRestoreNote() {
        // Arrange
        val note = service.createNote("Title", "Text")

        // Act
        service.deleteNote(note.id)
        val deletedNote = service.getNote(note.id)
        val restoredNote = service.restoreNote(note.id)

        // Assert
        assertNull(deletedNote)
        assertNotNull(restoredNote)
        assertFalse(restoredNote.isDeleted)
    }

    @Test
    fun createCommentForNote() {
        // Arrange
        val note = service.createNote("Title", "Text")
        val commentText = "Comment text"

        // Act
        val comment = service.createComment(note.id, commentText)
        val retrievedComment = service.getComment(comment.id)

        // Assert
        assertNotNull(retrievedComment)
        assertEquals(commentText, retrievedComment?.text)
    }

    @Test
    fun updateComment() {
        // Arrange
        val note = service.createNote("Title", "Text")
        val comment = service.createComment(note.id, "Old comment")

        // Act
        val updatedComment = service.updateComment(comment.id, "Updated comment")

        // Assert
        assertEquals("Updated comment", updatedComment.text)
    }

    @Test
    fun deleteAndRestoreComment() {
        // Arrange
        val note = service.createNote("Title", "Text")
        val comment = service.createComment(note.id, "Comment text")

        // Act
        service.deleteComment(comment.id)
        val deletedComment = service.getComment(comment.id)
        val restoredComment = service.restoreComment(comment.id)

        // Assert
        assertNull(deletedComment)
        assertNotNull(restoredComment)
        assertFalse(restoredComment.isDeleted)
    }

    @Test (expected = EntityDeletedException::class)
    fun attemptToUpdateDeletedNoteShouldThrowException() {
        // Arrange
        val note = service.createNote("Title", "Text")
        service.deleteNote(note.id)

        // Act & Assert
        service.updateNote(note.id, "Title", "Text")
    }

    @Test (expected = EntityDeletedException::class)
    fun attemptToDeleteAlreadyDeletedNoteShouldThrowException() {
        // Arrange
        val note = service.createNote("Title", "Text")
        service.deleteNote(note.id)

        // Act & Assert
        service.deleteNote(note.id)
    }

    @Test (expected = EntityDeletedException::class)
    fun attemptToRestoreNonDeletedNoteShouldThrowException() {
        // Arrange
        val note = service.createNote("Title", "Text")

        // Act & Assert
        service.restoreNote(note.id)
    }

    @Test (expected = EntityDeletedException::class)
    fun attemptToUpdateDeletedCommentShouldThrowException() {
        // Arrange
        val note = service.createNote("Title", "Text")
        val comment = service.createComment(note.id, "Comment")
        service.deleteComment(comment.id)

        // Act & Assert
        service.updateComment(comment.id, "Updated text")
    }

    @Test (expected = NoteNotFoundException::class)
    fun attemptToDeleteNonExistentNoteShouldThrowException() {
        // Act & Assert
        service.getNote(9999)
    }

    @Test (expected = NoteNotFoundException::class)
    fun attemptToCreateCommentForNonExistentNoteShouldThrowException() {
        // Act & Assert
        service.createComment(9999, "Comment text")
    }

    @Test
    fun deleteNoteShouldAlsoDeleteAllItsComments() {
        // Arrange
        val note = service.createNote("Title", "Text")
        val comment1 = service.createComment(note.id, "Comment 1")
        val comment2 = service.createComment(note.id, "Comment 2")

        // Act
        service.deleteNote(note.id)

        // Assert
        assertNull(service.getNote(note.id))
        assertNull(service.getComment(comment1.id))
        assertNull(service.getComment(comment2.id))
    }
}
