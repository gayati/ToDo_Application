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
import java.nio.file.Paths
import java.util.Date
import scala.util.Success
import scala.util.Failure
import model.Label
import scala.concurrent.duration._
import scala.concurrent.Await
import model.CreateNoteDto
import service.IUserService
import service.ICollaberatorService
import akka.actor.ActorSystem
//import com.beachape.metascraper.Messages._
//import com.beachape.metascraper.ScraperActor
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import model.scrapDto

@Singleton
class NoteController @Inject() (noteService: INoteService, collaberatorService: ICollaberatorService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createNote() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var note: CreateNoteDto = json.as[CreateNoteDto]
      println("fgfdgdfgggfdggfg" + note)
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
      println(CreateNoteDto.toString() + "Cretate..................")
      var note: CreateNoteDto = json.as[CreateNoteDto]
      println("reminder.........." + note.reminder)
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
      var a = 0
      var x = Seq[NoteDto]()

      for (a <- 0 until notes.length by 1) {

        var note = notes(a)
        println(note + " note..................dsfsdfd")
        var labelList = noteService.getNoteLabels(note.noteId)

        var data = Await.result(labelList, 1 second)

        var collaberatorList = collaberatorService.getCollaberator(note.noteId);
        var data1 = Await.result(collaberatorList, 1 second)
        println(data1 + "list of collaberattor..............................................")
        println(note.imageLink + "rtttttttttttttttttt")
        var noteDto = NoteDto(note.noteId, note.title, note.description, note.color, note.isArchived,
          note.isPinned, note.isTrashed, note.reminder, note.remindertime, note.image, data, note.createdBy, 
          data1,note.showLink,note.scrapUrl:String,note.urlTitle:String,note.imageLink:String)
        println(noteDto + "noteDto.................................")
        x = x :+ (noteDto)
        println(x + "final list of notes.......................")

      }

      Ok(Json.toJson(x))

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

  def getNoteLabel(noteId: Int) = Action.async { implicit request: Request[AnyContent] =>
    noteService.getNoteLabels(noteId) map { notes =>
      notes
      println(notes)
      Ok(Json.toJson(notes))
    }
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("file").map { picture =>
      print("In upload............+ " + picture)
      val filename = Paths.get(picture.filename).getFileName
      println("File uplo..aded..................." + filename)
      picture.ref.moveTo(Paths.get(s"/home/bridgeit/Documents/scala-project/PlaySampleProject/todo_app/app/tmp/$filename"), replace = true)
      Ok("http://localhost:9000/image/" + filename)
    }.getOrElse {
      BadRequest("Missing File")
    }
  }

  def serveUploadedFiles2(file: String) = Action {
    print(file + "ServeUploadFile...............")
    Ok.sendFile(
      content = new java.io.File("/home/bridgeit/Documents/scala-project/PlaySampleProject/todo_app/app/tmp/" + file),
      fileName = _ => file)
  }

  def removeLabel(noteId: Int, labelId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    println(labelId + "In remove Label........................")
    noteService.removeLabel(noteId, labelId) map { deleteFuture =>
      Ok(deleteFuture)
    }
  }

  import java.net.URL
  import org.jsoup.Jsoup
  import scala.collection.JavaConversions._

  def addUrl(url:String) = Action{ implicit request: Request[AnyContent] =>
    val timeout = 10000
   val url1 = new URL(url)
    var imgTags = Jsoup.parse(url1, timeout).select("meta[property=og:image]")
    var imgSources = imgTags.map(_.attributes.get("content")).toList
    

    
    var title = Jsoup.parse(url1, timeout).select("meta[property=og:title]")
    var titleSources = title.map(_.attributes.get("content")).toList
    
    val c = imgSources ++ titleSources

//   var scrapDto1 =scrapDto(imgSources.toString(),titleSources.toString())
//    noteService.updateNote(noteId:Int, token:String, noteDto:)
     Ok(Json.toJson(c));
// 
  }

}