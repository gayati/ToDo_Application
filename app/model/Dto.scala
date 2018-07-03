package model

import play.api.libs.json.Json

case class RegisterDto(username: String, emailId: String, password: String)

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

case class NoteDto(title: String, description: String)

object NoteDto {
  implicit val NoteDto = Json.format[NoteDto]
}

