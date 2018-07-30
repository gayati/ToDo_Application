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
import java.util.Date
import model.NoteLabel
import model.Label

@Singleton
class NoteService @Inject() (noteDao: INoteDao, userDao: IUserDao, jwtToken: JwtToken)(implicit ec: ExecutionContext) extends INoteService {

  override def createNote(note: NoteDto, token: String): Future[String] = {
    val id = jwtToken.getTokenId(token)
    userDao.getUserById(id) map { userFuture =>
      if (!(userFuture.equals(None))) {
        val user = userFuture.get
        val date: Date = new Date(System.currentTimeMillis())
        val note1 = Note(0, note.title, note.description, date, date, note.color, note.isArchived,
          note.isPinned, note.isTrashed, user.id,None)
        noteDao.createNote(note1) map { createNoteFuture =>
          createNoteFuture
          "Note Created successfully"
        }
        "Note Created successfully"
      } else {
        "User not found"
      }
    }
  }

  override def deleteNote(noteId: Int, token: String): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    noteDao.getNoteById(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        val note = noteFuture.get
        // val userId = note.createdBy
        if (uId == note.createdBy) {
          noteDao.deleteNote(noteId) map { deletenoteFuture =>
            deletenoteFuture
           "DeleteSuccess"
          }
        } else {
         "DeleteNotSuccess"
        }
       "DeleteSuccess"
      } else {
        "DeleteNotSuccess"
      }
    }
  }

  override def updateNote(noteId: Int, token: String, noteDto: NoteDto): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    noteDao.getNoteById(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        var note = noteFuture.get
        if (uId == note.createdBy) {
        var date: Date = new Date(System.currentTimeMillis())
        // note.updatedDate = date
        var date1 = note.updatedDate
        date1 = date
        note = Note(note.noteId, noteDto.title, noteDto.description, note.createdDate, date1, noteDto.color,
          noteDto.isArchived, noteDto.isPinned, noteDto.isTrashed,note.createdBy,None)
        var result: String = ""
        noteDao.updateNote(note) map { updatenoteFuture =>
          "success"
          
        }
                 "success"

         } else { "Acesss denied........." }
      } else { "Note not found" }
    }
  }

  override def getNotes(token: String): Future[Seq[Note]] = {
    val userId = jwtToken.getTokenId(token)
    noteDao.getNotes(userId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        noteFuture
        
      } else {
       null
      }
    }
  }
  
    override def addnoteLabel(noteLabel:NoteLabel):Future[String] ={
      val note = NoteLabel(noteLabel.noteId,noteLabel.labelId)
    noteDao.addNoteLabel(noteLabel) map {addLabeFuture =>
          addLabeFuture
          "CreateSuccess"
    }
  } 
    
    override def getNoteLabels(noteId:Int): Future[Seq[Label]] = {
    noteDao.getNoteLabels(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        noteFuture
      } else {
       null
      }
    }
  }
    
    

}