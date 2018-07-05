package dao

import model.Note
import scala.concurrent.Future
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[NoteDao])
trait INoteDao {
  def createNote(note: Note): Future[Int]
  def deleteNote(noteId: Int): Future[Int]
  def updateNote(note: Note): Future[Int]
  def getNoteBytitle(title: String): Future[Option[Note]]
  def getNoteById(id: Int): Future[Option[Note]]
  def getNotes(userId:Int): Future[Seq[Note]]
}