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
    noteDao.createNote(note) map { createNoteFuture => createNoteFuture
    "Note Created successfully"
    }
  }
  
  
  override def deleteNote(title:String):Future[String]={
   noteDao.deleteNote(title) map { deletenoteFuture => deletenoteFuture 
    "User Successfully deleted..."  
   }
  }
}