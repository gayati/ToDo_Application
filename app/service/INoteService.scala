package service

import model.NoteDto
import scala.concurrent.Future
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[NoteService])
trait INoteService {
      def createNote(noteDto:NoteDto): Future[String]
      def deleteNote(title:String):Future[String]

}