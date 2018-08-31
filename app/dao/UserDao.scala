package dao

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import javax.inject.Singleton
import play.api.db.Database
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import model.User
import scala.concurrent.Future
import model.Note
import model.NoteLabel
import model.Label
import java.util.Date
import model.Collaberator

@Singleton
class UserDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends IUserDao {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  implicit val JavaUtilDateMapper =
    MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))

  private class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def mobileNumber = column[String]("mobile_number")

    def email = column[String]("email")

    def passWord = column[String]("password")

    def isVerified = column[Boolean]("isverified")

    def profileImage = column[Option[String]]("profile_image")

    override def * = (id, firstName, lastName, mobileNumber, email, passWord, isVerified, profileImage) <> ((User.apply _).tupled, User.unapply)

  }

  private val users = TableQuery[UserTable]

  override def register(user: User): Future[Int] = {
    val action = ((users returning users.map(_.id)) += user)
    db.run(action).map(id => id)
  }

  override def isExist(email: String): Future[Option[User]] = {
    db.run(users.filter(_.email === email).result.headOption)

  }

  override def login(user: User): Future[Option[User]] = {
    db.run((users.filter(_.email === user.emailId)).result.headOption)
  }

  override def getUsetByEmail(email: String): Future[Option[User]] = {
    db.run((users.filter(_.email === email).result.headOption))
  }

  def getUserById(id: Int): Future[Option[User]] = {
    db.run((users.filter(_.id === id)).result.headOption)
  }
  override def update(user: User): Future[Int] = {
    println(user.toString() + "user updated")
    db.run(users.filter(_.id === user.id).update(user))
  }

  override def getUserByemail(emailid: String): Future[Option[User]] = {
    db.run(users.filter(_.email === emailid).result.headOption)
  }

  //    override def updateUser(user:User):Future[Int]={
  //    db.run(users.filter(_.id === user.id).update(user))
  //    }

  def all: Future[Seq[User]] = db.run(users.result)

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

    def colaberator = column[Int]("collaberator")
    
    def showlink = column[Boolean]("showLink")
    
    def scrapUrl = column[String]("scrap_url")
    
    def urlTitle = column[String]("url_title")
    
    def imageLink = column[String]("image_link")

    // def collaberatedUser = foreignKey("userId", sharedTo, collaberators)(_.sharedTo)

   
    override def * = (noteId, title, description, createddate, updatedDate, color, isarchived, ispinned, istrashed, createdBy, reminder, remindertime, image, colaberator,showlink,scrapUrl,urlTitle,imageLink) <> ((Note.apply _).tupled, Note.unapply)
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

  //   override def getNoteById1(id: Int): Future[Option[Note]] = {
  //    db.run(notes.filter((_.noteId === id)).result.headOption)
  //  }

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

  override def addNoteLabel(noteLabel: NoteLabel): Future[Int] = {
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

  def removeLabel(noteId: Int, labelId: Int): Future[Int] = {
    //         db.run(notesLabel.filter(_.noteId === noteId).delete)

    db.run(notesLabel.filter(value => value.noteId === noteId
      && value.labelId === labelId).delete)
  }

  private class CollaberatorTabel(tag: Tag) extends Table[Collaberator](tag, "Collaberator") {

    def collaberatorId = column[Int]("collaberator_id", O.PrimaryKey, O.AutoInc)

    def sharedBy = column[Int]("shared_by")

    def sharedTo = column[Int]("shared_to")

    def noteId = column[Int]("note_id")

    override def * = (collaberatorId, sharedBy, sharedTo, noteId) <> ((Collaberator.apply _).tupled, Collaberator.unapply)
  }

  private val collaberators = TableQuery[CollaberatorTabel]

  override def addCollaberator(collaberator: Collaberator): Future[Int] = {
    val action = ((collaberators returning collaberators.map(_.collaberatorId)) += collaberator)
     db.run(action) map { Id => Id }
      }

  override def getCollaberator(noteId: Int): Future[Seq[User]] = {
    db.run {
      val innerJoin = for {
        (ab, a) <- collaberators join users on (_.sharedTo === _.id)
      } yield (a, ab.noteId)
      innerJoin.filter(_._2 === noteId).map(_._1).result
    }
  }

  override def getCollaberatedNote(sharedTo: Int): Future[Seq[Collaberator]] = {
    db.run(collaberators.filter(_.sharedTo === sharedTo).result)
  }

  def removeCollaberator(noteId: Int, userId: Int): Future[Int] = {
    db.run(collaberators.filter(value => value.noteId === noteId
      && value.sharedBy === userId).delete)
  }

}