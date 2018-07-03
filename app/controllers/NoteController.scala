package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc._
import play.api.mvc.ControllerComponents
import scala.concurrent.ExecutionContext
import play.api.mvc.AbstractController
import model.NoteDto
import service.INoteService
import scala.concurrent.Future
import play.api.libs.json.Json
import model.Note

@Singleton
class NoteController @Inject() (noteService: INoteService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createNote() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var note: Note = json.as[Note]
      noteService.createNote(note, token) map { createnoteFuture =>
        Ok(createnoteFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  def deleteNote(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    noteService.deleteNote(noteId,token) map { deleteFuture =>
      Ok(deleteFuture)
    }
  }

  def updateNote(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var note: Note = json.as[Note]
      noteService.updateNote(note, noteId) map { updateFuture =>
        Ok(updateFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  //def getNote() = Action.async(block)
}