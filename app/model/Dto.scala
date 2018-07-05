package model

import play.api.libs.json.Json
import java.sql.Date

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

case class NoteDto(title: String, description: String,color:String,isArchived:Boolean,
    isPinned:Boolean,
    isTrashed:Boolean)


object NoteDto {
  implicit val NoteDto = Json.format[NoteDto]
}

