package service

import model.NoteDto
import scala.concurrent.Future
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[NoteService])
trait INoteService {
  def createNote(noteDto: NoteDto): Future[String]
  def deleteNote(noteDto: NoteDto): Future[String]
  def updateNote(note:NoteDto): Future[String]

}