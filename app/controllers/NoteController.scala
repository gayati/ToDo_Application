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

@Singleton
class NoteController @Inject() (noteService:INoteService,cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createNote() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var note: NoteDto = json.as[NoteDto]
      noteService.createNote(note) map { createnoteFuture =>
        Ok(createnoteFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }
  
  def deleteNote() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var note: NoteDto = json.as[NoteDto]
      noteService.deleteNote(note.title) map { deleteFuture =>
        Ok(deleteFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }
}