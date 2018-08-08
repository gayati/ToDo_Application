package dao

import model.Note
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.Label
import model.NoteLabel

@ImplementedBy(classOf[NoteDao])
trait INoteDao {
  def createNote(note: Note): Future[Int]
  def deleteNote(noteId: Int): Future[Int]
  def updateNote(note: Note): Future[Int]
  def getNoteBytitle(title: String): Future[Option[Note]]
  def getNoteById(id: Int): Future[Option[Note]]
  def getNotes(userId:Int): Future[Seq[Note]]
  def addLabel(label:Label):Future[Int]
  def getLabels(userId:Int):Future[Seq[Label]]
  def getLabelById(labelId:Int):Future[Option[Label]]
  def deleteLabel(labelId:Int):Future[Int]
  def updateLabel(label:Label):Future[Int]
  def addNoteLabel(noteLabel:NoteLabel):Future[Int]
  def getNoteLabels(noteId: Int): Future[Seq[Label]]
   def removeLabel(noteId:Int):Future[Int]
}