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

@Singleton
class NoteDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends INoteDao {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class NoteTable(tag: Tag) extends Table[Note](tag, "Notes") {

    def Id = column[Int]("note_id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("note_title")

    def description = column[String]("note_descp")

    def isarchived = column[Boolean]("isArchived")

    def ispinned = column[Boolean]("isPinned")

    def istrashed = column[Boolean]("isTrashed")

    def createBy = column[Int]("created_by")

    override def * = (Id, title, description, isarchived, ispinned, istrashed, createBy) <> ((Note.apply _).tupled, Note.unapply)
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

}