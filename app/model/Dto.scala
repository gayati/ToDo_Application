package model

import play.api.libs.json.Json
import java.util.Date

case class RegisterDto(firstName:String,lastName:String,mobileNumber:String, emailId: String, password: String)

object RegisterDto {
  implicit val RegisterDto = Json.format[RegisterDto]
}
case class LoginDto(emailId: String, password: String)

object LoginDto {
  implicit val LoginDto = Json.format[LoginDto]
}

case class ForgotPasswordDto(emailId: String)

object ForgotPasswordDto {
  implicit val ForgotPasswordDto = Json.format[ForgotPasswordDto]
}

case class PasswordDto(password: String)

object PasswordDto {
  implicit val PasswordDto = Json.format[PasswordDto]
}

case class NoteDto(noteId:Int,title: String, description: String,color:String,isArchived:Boolean,
    isPinned:Boolean,
    isTrashed:Boolean,reminder:Option[Date],remindertime:Option[String],image:Option[String],
    labelList:Seq[Label],createdBy:Int)


object NoteDto {
  implicit val NoteDto = Json.format[NoteDto]
}

case class CollabNoteDto(noteId:Int,title: String, description: String,color:String,isArchived:Boolean,
    isPinned:Boolean,
    isTrashed:Boolean,reminder:Option[Date],remindertime:Option[String],image:Option[String],
    labelList:Seq[Label],createdBy:Int,collaberatorList:Seq[User])


object CollabNoteDto {
  implicit val NoteDto = Json.format[CollabNoteDto]
}

case class CreateNoteDto(title: String, description: String,color:String,isArchived:Boolean,
    isPinned:Boolean,
    isTrashed:Boolean,reminder:Option[Date],remindertime:Option[String],image:Option[String],labelList:Seq[Label])


object CreateNoteDto {
  implicit val CreateNoteDto = Json.format[CreateNoteDto]
}



case class CollaberatorDto(sharedBy:Int,sharedTo:Int,noteId:Int)

object CollaberatorDto{
  implicit val CollaberatorDto = Json.format[CollaberatorDto]
}





