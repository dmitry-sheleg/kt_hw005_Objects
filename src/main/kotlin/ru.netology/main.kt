package ru.netology

// Интерфейс для сущностей, которые могут быть удалены
interface CanDelete {
    var isDeleted: Boolean
}

// Класс базовой абстрактной сущности
abstract class BaseEntity(
    open val id: Int,
    val date: Long = System.currentTimeMillis()
)

// Обобщённая сущность от класса `BaseEntity` и реализующая интерфейс удаления
abstract class CanDeleteEntity(id: Int) : BaseEntity(id), CanDelete {
    override var isDeleted: Boolean = false
}

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

interface Attachment {
    val type: String
}

data class AttachmentAudio(
    override val type: String = "audio",
    val audio: Audio
) : Attachment

data class AttachmentVideo(
    override val type: String = "video",
    val video: Video
) : Attachment

data class AttachmentPhoto(
    override val type: String = "photo",
    val photo: Photo
) : Attachment

data class AttachmentLink(
    override val type: String = "link",
    val link: Link
) : Attachment

data class AttachmentNote(
    override val type: String = "note",
    val note: Note
) : Attachment

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

// Класс модели заметки
data class Note(
    override val id: Int,
    val ownerID: Int? = null,
    val comment: Int? = null,
    val readComments: Int? = null,
    val viewURL: String? = null,
    var title: String,
    var text: String,
    var comments: MutableList<Comment> = mutableListOf()
) : CanDeleteEntity(id)

// Класс модели комментария
data class Comment(
    override val id: Int = 0,
    val fromId: Int? = null,
    val postId: Long? = null,
    val replyToUser: Long? = null,
    val replyToComment: Long? = null,
    var noteId: Int = 0, // ID родительской заметки
    var text: String
) : CanDeleteEntity(id) {
    // Проверяем, принадлежит ли комментарий указанной заметке
    fun belongsTo(noteId: Int): Boolean = this.noteId == noteId
}

// Хранилище для работы с обобщёнными сущностями
class EntityStorage<T : CanDeleteEntity> {
    val entities = mutableMapOf<Int, T>()

    // Создание новой сущности
    fun create(entity: T): T {
        entities[entity.id] = entity
        return entity
    }

    // Получение сущности по ID (только активных)
    fun get(id: Int): T? = entities[id]?.takeIf { !it.isDeleted }

    // Обновление сущности
    fun update(entity: T): T {
        if (entity.isDeleted) {
            throw EntityDeletedException("Невозможно обновление удаленной сущности с ID: ${entity.id}")
        }
        entities[entity.id] = entity
        return entity
    }

    // Мягкое удаление сущности
    fun delete(id: Int): T {
        val entity = entities[id] ?: throw EntityNotFoundException("Сущность с ID $id не найдена")
        if (entity.isDeleted) {
            throw EntityDeletedException("Сущность с ID $id уже удалена")
        }
        entity.isDeleted = true
        return entity
    }

    // Восстановление сущности
    fun restore(id: Int): T {
        val entity = entities[id] ?: throw EntityNotFoundException("Сущность с ID $id не найдена")
        if (!entity.isDeleted) {
            throw EntityNotDeletedException("Сущность с ID $id не была помечена как удаленная")
        }
        entity.isDeleted = false
        return entity
    }

    // Получение всех активных сущностей
    fun getAll(): List<T> = entities.values.filter { !it.isDeleted }.toList()
}

// Базовое исключение для сущностей
open class EntityException(message: String) : RuntimeException(message)

// Исключение: сущность не найдена
open class EntityNotFoundException(message: String) : EntityException(message)

// Исключение: сущность удалена
class EntityDeletedException(message: String) : EntityException(message)

// Исключение: сущность не удалена
class EntityNotDeletedException(message: String) : EntityException(message)

// Специфические исключения
class NoteNotFoundException(message: String) : EntityNotFoundException(message)
class CommentNotFoundException(message: String) : EntityNotFoundException(message)

class PostNotFoundException(message: String) : Exception(message)

class NotesService {
    private var notesStorage = EntityStorage<Note>()
    private var commentsStorage = EntityStorage<Comment>()

    // Создание заметки
    fun createNote(title: String, text: String): Note {
        val note = Note(id = generateId(), title = title, text = text)
        return notesStorage.create(note)
    }

    // Получение заметки
    fun getNote(noteId: Int): Note? = notesStorage.get(noteId)

    // Обновление заметки
    fun updateNote(noteId: Int, title: String, text: String): Note {
        val note = notesStorage.entities[noteId]
            ?: throw NoteNotFoundException("Note with ID $noteId not found")
        if (note.isDeleted) {
            throw EntityDeletedException("Невозможно обновление удалённой заметки с ID: $noteId")
        }
        note.title = title
        note.text = text
        return notesStorage.update(note)
    }

    // Удаление заметки (и всех её комментариев)
    fun deleteNote(noteId: Int): Note {
        val note = notesStorage.entities[noteId]
            ?: throw NoteNotFoundException("Note with ID $noteId not found")
        if (note.isDeleted) {
            throw EntityDeletedException("Невозможно удаление уже удалённой заметки с ID: $noteId")
        }
        // Помечаем все комментарии заметки как удалённые
        note.comments.forEach { comment ->
            commentsStorage.delete(comment.id)
        }

        return notesStorage.delete(noteId)
    }

    // Восстановление заметки
    fun restoreNote(noteId: Int): Note = notesStorage.restore(noteId)

    // Создание комментария к заметке
    fun createComment(noteId: Int, text: String): Comment {
        val note = notesStorage.get(noteId) ?: throw NoteNotFoundException("Note with ID $noteId not found")

        val comment = Comment(id = generateId(), noteId = noteId, text = text)
        commentsStorage.create(comment)
        note.comments.add(comment)

        return comment
    }

    // Получение комментария
    fun getComment(commentId: Int): Comment? = commentsStorage.get(commentId)

    // Обновление комментария
    fun updateComment(commentId: Int, text: String): Comment {
        val comment = commentsStorage.entities[commentId]
            ?: throw CommentNotFoundException("Comment with ID $commentId not found")
        if (comment.isDeleted) {
            throw EntityDeletedException("Невозможно обновление удалённого комментария с ID: $commentId")
        }
        comment.text = text
        return commentsStorage.update(comment)
    }

    // Удаление комментария
    fun deleteComment(commentId: Int): Comment = commentsStorage.delete(commentId)

    // Восстановление комментария
    fun restoreComment(commentId: Int): Comment = commentsStorage.restore(commentId)

    // Вспомогательные методы
    private fun generateId(): Int = (1..10000).random()

    fun clear() {
        notesStorage = EntityStorage<Note>()
        commentsStorage = EntityStorage<Comment>()
    }
}

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
        comments = emptyArray()
        uniqueID = 1
    }

    // Метод для доступа к данным (для тестов)
    fun getComments(): Array<Comment> = comments
}