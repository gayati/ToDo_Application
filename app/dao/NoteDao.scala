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

@Singleton
class NoteDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends INoteDao {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class NoteTable(tag: Tag) extends Table[Note](tag, "Notes") {

    def Id = column[Int]("noteId", O.PrimaryKey, O.AutoInc)

    def title = column[String]("note_title")

    def description = column[String]("note_descp")

    override def * = (Id, title, description) <> ((Note.apply _).tupled, Note.unapply)
  }

  private val notes = TableQuery[NoteTable]
  
  override def createNote(note: Note):Future[Int] = {
  val action = ((notes returning notes.map(_.Id)) += note)
    db.run(action) map{Id =>Id}
  }
  
 override def deleteNote(title:String):Future[Int]={
      db.run(notes.filter(_.title === title).delete)

  }
  
}