package dao

import model.Note
import play.api.db.Database
import javax.inject
import javax.inject.Singleton
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import scala.concurrent.Future
import model.NoteDto
import java.util.Date
import model.Label
import model.NoteLabel
import play.api.mvc.Result

@Singleton
class NoteDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends INoteDao {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  implicit val JavaUtilDateMapper =
    MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))

  private class NoteTable(tag: Tag) extends Table[Note](tag, "Note") {

    def noteId = column[Int]("note_id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("note_title")

    def description = column[String]("note_descp")

    def createddate = column[Date]("created_date")

    def updatedDate = column[Date]("updated_date")

    def color = column[String]("color")

    def isarchived = column[Boolean]("isArchived")

    def ispinned = column[Boolean]("isPinned")

    def istrashed = column[Boolean]("isTrashed")

    def createdBy = column[Int]("created_by")

    def reminder = column[Option[Date]]("remin_der")
    
    def remindertime = column[Option[String]]("reminder_time")
    
    def image = column[Option[String]]("uploaded_image")
    
//    def labelList = column[[String]]("")
  
    override def * = (noteId, title, description, createddate, updatedDate, color, isarchived, ispinned, istrashed, createdBy, reminder,remindertime,image) <> ((Note.apply _).tupled, Note.unapply)
  }

  private val notes = TableQuery[NoteTable]

  private class LabelTabel(tag: Tag) extends Table[Label](tag, "Label") {

    def labelId = column[Int]("label_id", O.PrimaryKey, O.AutoInc)

    def labelTitle = column[String]("label_title")

    def userId = column[Int]("user_id")

    override def * = (labelId, labelTitle, userId) <> ((Label.apply _).tupled, Label.unapply)
  }

  private val labels = TableQuery[LabelTabel]
  

  private class notesLabelTable(tag: Tag) extends Table[NoteLabel](tag, "NoteLabel") {

    def noteId = column[Int]("noteId")

    def labelId = column[Int]("labelId")

//    def note = foreignKey("noteId", noteId, notesTable)(_.noteId)
//
//    def label = foreignKey("labelId", labelId, labels)(_.labelId)

    def * = (noteId, labelId) <> ((NoteLabel.apply _).tupled, NoteLabel.unapply)

  }
  
  private val notesLabel = TableQuery[notesLabelTable]

  override def createNote(note: Note): Future[Int] = {
    val action = ((notes returning notes.map(_.noteId)) += note)
    db.run(action) map { Id => Id }
  }

  override def deleteNote(noteId: Int): Future[Int] = {
    db.run(notes.filter(_.noteId === noteId).delete)
  }

  override def getNoteById(id: Int): Future[Option[Note]] = {
    db.run(notes.filter((_.noteId === id)).result.headOption)
  }

  override def getNoteBytitle(title: String): Future[Option[Note]] = {
    db.run(notes.filter((_.title === title)).result.headOption)
  }

  override def updateNote(note: Note): Future[Int] = {
    db.run(notes.filter(_.noteId === note.noteId).update(note))
  }

  override def getNotes(userId: Int): Future[Seq[Note]] = {
    db.run(notes.filter((_.createdBy === userId)).result)
  }

  override def addLabel(label: Label): Future[Int] = {
    val action = ((labels returning labels.map(_.labelId)) += label)
    db.run(action) map { Id => Id }
  }

  override def getLabels(uId: Int): Future[Seq[Label]] = {
    db.run(labels.filter((_.userId === uId)).result)
  }

  override def deleteLabel(labelId: Int): Future[Int] = {
    db.run(labels.filter((_.labelId === labelId)).delete)
  }

  override def getLabelById(labelId: Int): Future[Option[Label]] = {
    db.run(labels.filter((_.labelId === labelId)).result.headOption)
  }

  override def updateLabel(label: Label): Future[Int] = {
    db.run(labels.filter(_.labelId === label.labelId).update(label))
  }

  override def addNoteLabel(noteLabel: NoteLabel):Future[Int] = {
   db.run(notesLabel += noteLabel)
  }
  
 
  def getNoteLabels(noteId: Int): Future[Seq[Label]] = {
    db.run {
      val innerJoin = for {
        (ab, a) <- notesLabel join labels on (_.labelId === _.labelId)
      } yield (a, ab.noteId)
      innerJoin.filter(_._2 === noteId).map(_._1).result
    }
  }
  
   def removeLabel(noteId:Int,labelId:Int):Future[Int]={
//         db.run(notesLabel.filter(_.noteId === noteId).delete)

    db.run(notesLabel.filter(value => value.noteId === noteId 
         && value.labelId === labelId).delete)

    
   }

}