package service

import model.NoteDto
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.Note
import model.NoteLabel
import model.Label
import play.api.mvc.Request
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData
import model.CreateNoteDto
import model.Collaberator


@ImplementedBy(classOf[NoteService])
trait INoteService {
  def createNote(note: CreateNoteDto,toekn:String): Future[String]
  def deleteNote(noteId: Int,token:String): Future[String]
  def updateNote(noteId:Int,token:String,noteDto:CreateNoteDto): Future[String]
  def getNotes(token:String):Future[Seq[Note]]
  def addnoteLabel(noteLabel:NoteLabel):Future[String]
  def getNoteLabels(noteId:Int): Future[Seq[Label]]
  def removeLabel(noteId:Int,labelId:Int):Future[String]
  def getCollaberatedNotes(noteId:Int):Future[Seq[Collaberator]]
   def getNoteById(id: Int): Future[Option[Note]]

}