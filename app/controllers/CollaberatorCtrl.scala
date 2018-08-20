package controllers

import javax.inject.Inject
import service.ICollaberatorService
import scala.concurrent.ExecutionContext
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject.Singleton
import play.api.mvc.Request
import play.api.mvc.AnyContent
import scala.concurrent.Future
import model.CollaberatorDto
import play.api.libs.json.Json
import play.api.mvc.Action
import model.NoteDto
import service.INoteService
import scala.concurrent.Await
import scala.concurrent.duration._
import model.CollabNoteDto
import service.IUserService

@Singleton
class CollaberatorCtrl @Inject() (collaberatorService: ICollaberatorService, noteService: INoteService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addCollaberator() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var collaberator: CollaberatorDto = json.as[CollaberatorDto]
      collaberatorService.addCollaberator(collaberator) map { addFuture =>
        Ok(addFuture)
      }
    }.getOrElse(Future {
      BadRequest("Registration Failed..!!")
    })
  }

  def getCollaberator(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    
    
    collaberatorService.getCollaberator(noteId) map { collaberators =>
      collaberators
      println(collaberators)
      Ok(Json.toJson(collaberators))
    }
  }
 
  def getCollaberatedNotes(sharedTo: Int) = Action.async { implicit request: Request[AnyContent] =>
    collaberatorService.getCollaberatedNotes(sharedTo) map { collaberatedNote =>
      collaberatedNote
      var a = 0
      var x = Seq[NoteDto]()
      for (a <- 0 until collaberatedNote.length by 1) {
        var note = collaberatedNote(a)

        var note1 = noteService.getNoteById(note.noteId)
        var data = Await.result(note1, 1 second)
        if (data != None) {
          var note2 = data.get
          println("in collaberator ctrl...dfdfsd........" + note2)

          var labelList = noteService.getNoteLabels(note.noteId)
          var labellist = Await.result(labelList, 1 second)

          var collaberatorList = collaberatorService.getCollaberator(note.noteId);
          var data1 = Await.result(collaberatorList, 1 second)

          var noteDto = NoteDto(note2.noteId, note2.title, note2.description, note2.color, note2.isArchived,
            note2.isPinned, note2.isTrashed, note2.reminder, note2.remindertime, note2.image, labellist, note2.createdBy,data1)
          println(noteDto + "noteDto.................................")
          x = x :+ (noteDto)
          println(x + "final list of notes.......................")
        }

      }
      Ok(Json.toJson(x))
    }
  }

}