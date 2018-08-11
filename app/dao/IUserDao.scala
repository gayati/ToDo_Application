package dao

import model.User
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.Note
import model.Label
import model.NoteLabel
import model.Collaberator

@ImplementedBy(classOf[UserDao])
trait IUserDao {
  def register(user: User): Future[Int]
  def isExist(email: String): Future[Option[User]]
  def login(user: User): Future[Option[User]]
  def getUserById(id:Int):Future[Option[User]]
  def getUsetByEmail(email: String): Future[Option[User]]
  def update(user:User):Future[Int]
  def getUserByemail(emailid:String):Future[Option[User]]
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
  def removeLabel(noteId:Int,labelId:Int):Future[Int]
  def addCollaberator(collaberator:Collaberator):Future[Int]
  def getCollaberator(collaberatorId:Int):Future[Seq[Collaberator]]
}  