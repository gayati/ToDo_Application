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
      var note: NoteDto = json.as[NoteDto]
      noteService.createNote(note, token) map { createnoteFuture =>
        Ok(createnoteFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  def deleteNote(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    noteService.deleteNote(noteId, token) map { deleteFuture =>
      Ok(deleteFuture)
    }
  }

  def updateNote(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var note: NoteDto = json.as[NoteDto]
      noteService.updateNote(noteId, token, note) map { updateFuture =>
        Ok(updateFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  def getNotes() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    noteService.getNotes(token) map { notes =>
      notes
      println(notes)
      Ok(Json.toJson(notes))
    }

  }
}