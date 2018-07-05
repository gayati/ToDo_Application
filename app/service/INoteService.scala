package service

import model.NoteDto
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.Note

@ImplementedBy(classOf[NoteService])
trait INoteService {
  def createNote(note: NoteDto,toekn:String): Future[String]
  def deleteNote(noteId: Int,token:String): Future[String]
  def updateNote(noteId:Int,token:String,noteDto:NoteDto): Future[String]
  def getNotes(token:String):Future[Seq[Note]]
}