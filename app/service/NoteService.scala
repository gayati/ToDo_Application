package service

import model.NoteDto
import scala.concurrent.Future
import javax.inject
import javax.inject.Singleton
import dao.INoteDao
import model.Note
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import dao.UserDao
import dao.IUserDao
import utilities.JwtToken
import utilities.RedisCache

@Singleton
class NoteService @Inject() (noteDao: INoteDao, userDao: IUserDao, jwtToken: JwtToken)(implicit ec: ExecutionContext) extends INoteService {

  override def createNote(note: Note, token: String): Future[String] = {
    val id = jwtToken.getTokenId(token)
    userDao.getUserById(id) map { userFuture =>
      val user = userFuture.get
      val note1 = Note(0, note.title, note.description, note.isArchived, note.isPinned, false, user.id)
      noteDao.createNote(note1) map { createNoteFuture =>
        createNoteFuture
        "Note Created successfully"
      }
      "Note Created successfully"
    }
  }

  override def deleteNote(noteId: Int, token: String): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    noteDao.getNoteById(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        val note = noteFuture.get
        val userId = note.createdBy
        if (uId == userId) {
          noteDao.deleteNote(noteId) map { deletenoteFuture =>
            deletenoteFuture
            "Note Successfully deleted..."
          }   
        } else {
          ""
        }
         "Note found"
      } else {
        "note note found"
      }
    }

  }

  override def updateNote(note1: Note, noteId: Int): Future[String] = {
    noteDao.getNoteById(noteId) map { noteFuture =>
      var note = noteFuture.get
      note = Note(note.noteId, note1.title, note1.description, false, false, false, 2)
      noteDao.updateNote(note) map { updatenoteFuture =>
        updatenoteFuture
        "Note Successfully updated..."
      }
      "Note Successfully updated..."
    }
  }
}