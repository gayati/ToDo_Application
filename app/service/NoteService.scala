package service

import model.NoteDto
import scala.concurrent.Future
import javax.inject
import javax.inject.Singleton
import dao.INoteDao
import model.Note
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import dao.UserDao
import dao.IUserDao
import utilities.JwtToken
import utilities.RedisCache
import java.util.Date
import model.NoteLabel
import model.Label
import play.api.mvc.Request
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile
import play.api.Logger
import model.CreateNoteDto
import model.Collaberator

@Singleton
class NoteService @Inject() ( userDao: IUserDao, jwtToken: JwtToken)(implicit ec: ExecutionContext) extends INoteService {

  override def createNote(note: CreateNoteDto, token: String): Future[String] = {
    val id = jwtToken.getTokenId(token)
    userDao.getUserById(id) map { userFuture =>
      if (!(userFuture.equals(None))) {
        val user = userFuture.get
        val date: Date = new Date(System.currentTimeMillis())
        //        var reminderDate = null;
        //        if(note.reminder != null){
        //
        //        var reminderDate = new Date(note.reminder)
        //        }
        val note1 = Note(0, note.title, note.description, date, date, note.color, note.isArchived,
          note.isPinned, note.isTrashed, user.id, note.reminder, note.remindertime, note.image,0)
        userDao.createNote(note1) map { createNoteFuture =>
          createNoteFuture
          "Note Created successfully"
        }
        "Note Created successfully"
      } else {
        "User not found"
      }
    }
  }

  override def deleteNote(noteId: Int, token: String): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    userDao.getNoteById(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        val note = noteFuture.get
        // val userId = note.createdBy
        if (uId == note.createdBy) {
          userDao.deleteNote(noteId) map { deletenoteFuture =>
            deletenoteFuture
            "DeleteSuccess"
          }
        } else {
          "DeleteNotSuccess"
        }
        "DeleteSuccess"
      } else {
        "DeleteNotSuccess"
      }
    }
  }

  override def updateNote(noteId: Int, token: String, noteDto: CreateNoteDto): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    userDao.getNoteById(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        var note = noteFuture.get
        if (uId == note.createdBy) {
          var date: Date = new Date(System.currentTimeMillis())
          // note.updatedDate = date
          var date1 = note.updatedDate
          date1 = date
          // var reminderDate = new Date(noteDto.reminder)
          // println("In jfddddkkgfgdfgbg"+note + "In jfddddkkgfgdfgbg")

          note = Note(note.noteId, noteDto.title, noteDto.description, note.createdDate, date1, noteDto.color,
            noteDto.isArchived, noteDto.isPinned, noteDto.isTrashed, note.createdBy, noteDto.reminder, noteDto.remindertime, noteDto.image,0)
          var result: String = ""
          userDao.updateNote(note) map { updatenoteFuture =>
            "success"

          }
          "success"
        } else { "Acesss denied........." }
      } else { "Note not found" }
    }
  }

  override def getNotes(token: String): Future[Seq[Note]] = {
    val userId = jwtToken.getTokenId(token)
    userDao.getNotes(userId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        noteFuture

      } else {
        null
      }
    }
  }

  override def addnoteLabel(noteLabel: NoteLabel): Future[String] = {
    val note = NoteLabel(noteLabel.noteId, noteLabel.labelId)
    userDao.addNoteLabel(noteLabel) map { addLabeFuture =>
      addLabeFuture
      "CreateSuccess"
    }
  }

  override def getNoteLabels(noteId: Int): Future[Seq[Label]] = {
    userDao.getNoteLabels(noteId) map { noteFuture =>
      if (!(noteFuture.equals(None))) {
        noteFuture
      } else {
        null
      }
    }
  }
  
    def removeLabel(noteId:Int,labelId:Int):Future[String]={
      userDao.removeLabel(noteId,labelId) map { deletFuture =>
        deletFuture
        "deleteSuccess"
      }
        
      
    }
    
    override def getCollaberatedNotes(noteId:Int):Future[Seq[Collaberator]]={
      userDao.getCollaberatedNote(noteId)  map{ future =>
        future
        
      }
    }
    
     override def getNoteById(id: Int): Future[Option[Note]]={
        userDao.getNoteById(id) map { getNoteFuture =>
          getNoteFuture
          
        } 
      }


  //override def uploadFile(request:Request[MultipartFormData[TemporaryFile]]):String ={
  //    request.body.file("file").map { picture =>
  //      import java.io.File
  //      val filename = picture.filename
  //      print(filename + "fdgggggggggggggggggggggggggggggg")
  //      val contentType = picture.contentType
  //      picture.ref.moveTo(new File(s"/home/bridgeit/Documents/scala-project/PlaySampleProject/todo_app/uploads/$filename"))
  //      "Fileuploaded"
  //    }.getOrElse {
  //      "Missing file"
  //    }
  //  }

}