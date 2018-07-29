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
import model.NoteLabel

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
   //   Ok(deleteFuture)
      deleteFuture match {
        case "DeleteSuccess"    => Ok("Delete success..........")
        case "DeleteNotSuccess" => Conflict("Delete failure.........")
      }
    }
  }

  def updateNote(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    println("Update nOte " + token)

    request.body.asJson.map { json =>
      var note: NoteDto = json.as[NoteDto]
      noteService.updateNote(noteId, token, note) map { updateFuture =>
        updateFuture match {
          case success => Ok("Update success............")
        }
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

     def addNoteLabel() = Action.async { implicit request: Request[AnyContent] =>
     request.body.asJson.map { json =>
      var noteLabel: NoteLabel = json.as[NoteLabel]
      noteService.addnoteLabel(noteLabel) map { addLabelFuture =>
        addLabelFuture match {
          case "CreateSuccess"    => Ok("label added successfully.........")
          case "CreateNotSuccess" => Conflict("Label not added..........")
        }
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
     
   }
     
     def getNoteLabel(noteId:Int) = Action.async { implicit request: Request[AnyContent] =>
    noteService.getNoteLabels(noteId) map { notes =>
      notes
      println(notes)
      Ok(Json.toJson(notes))
    }
  }
 
    
  
  
  
}