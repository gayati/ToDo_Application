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

@Singleton
class NoteService @Inject() (noteDao: INoteDao)(implicit ec: ExecutionContext) extends INoteService {

  override def createNote(noteDto: NoteDto): Future[String] = {
    val note = Note(0, noteDto.title, noteDto.description)
    noteDao.createNote(note) map { createNoteFuture =>
      createNoteFuture
      "Note Created successfully"
    }
  }

  override def deleteNote(note:NoteDto): Future[String] = {
     noteDao.getNoteBytitle(note.title) map { futureNote => futureNote 
     var note = futureNote.get
    noteDao.deleteNote(note.noteId) map { deletenoteFuture =>
      deletenoteFuture
      "Note Successfully deleted..."
    }
    "" 
     }
  }

  override def updateNote(note:NoteDto): Future[String] = {
     noteDao.getNoteBytitle(note.title) map { futureNote => futureNote 
     var note = futureNote.get
     note = Note(note.noteId,note.title,note.description)
     noteDao.updateNote(note) map { updatenoteFuture =>
      updatenoteFuture
      "Note Successfully updated..."
    }
    ""
    }
    
  }
}