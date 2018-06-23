package model

import play.api.libs.json.Json

case class RegisterDto(username:String, emailId: String,password:String)
  

  object RegisterDto {
    implicit val RegisterDto = Json.format[RegisterDto]
  }
  case class LoginDto( emailId: String,password:String)
  

  object LoginDto {
    implicit val LoginDto = Json.format[LoginDto]
  }
