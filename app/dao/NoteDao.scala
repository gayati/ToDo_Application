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

@Singleton
class NoteDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends INoteDao {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  
implicit val JavaUtilDateMapper =
    MappedColumnType .base[java.util.Date, java.sql.Timestamp] (
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))

  private class NoteTable(tag: Tag) extends Table[Note](tag, "Note") {

    def Id = column[Int]("note_id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("note_title")

    def description = column[String]("note_descp")
    
    def createddate = column[Date]("created_date")
    
    def updatedDate = column[Date]("updated_date")
    
    def color = column[String]("color")

    def isarchived = column[Boolean]("isArchived")

    def ispinned = column[Boolean]("isPinned")

    def istrashed = column[Boolean]("isTrashed")
    
    def createdBy = column[Int]("created_by")
    
     def reminder = column[Option[Date]]("reminder")

    override def * = (Id, title, description, createddate,updatedDate,color,isarchived, ispinned, istrashed, createdBy,reminder) <> ((Note.apply _).tupled, Note.unapply)
  }

  private val notes = TableQuery[NoteTable]

  override def createNote(note: Note): Future[Int] = {
    val action = ((notes returning notes.map(_.Id)) += note)
    db.run(action) map { Id => Id }
  }

  override def deleteNote(noteId: Int): Future[Int] = {
    db.run(notes.filter(_.Id === noteId).delete)
  }

  override def getNoteById(id: Int): Future[Option[Note]] = {
    db.run(notes.filter((_.Id === id)).result.headOption)
  }

  override def getNoteBytitle(title: String): Future[Option[Note]] = {
    db.run(notes.filter((_.title === title)).result.headOption)
  }

  override def updateNote(note: Note): Future[Int] = {
    db.run(notes.filter(_.Id === note.noteId).update(note))
  }

  override def getNotes(userId: Int): Future[Seq[Note]] = {
    db.run(notes.filter((_.createdBy === userId)).result)
  }

}