package service

import model.NoteDto
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.Note

@ImplementedBy(classOf[NoteService])
trait INoteService {
  def createNote(note: Note,toekn:String): Future[String]
  def deleteNote(noteId: Int,token:String): Future[String]
  def updateNote(note:Note,noteId:Int): Future[String]
  
}