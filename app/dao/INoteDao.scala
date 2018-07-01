package dao

import model.Note
import scala.concurrent.Future
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[NoteDao])
trait INoteDao {
    def createNote(note:Note):Future[Int]
    def deleteNote(title:String):Future[Int]

}