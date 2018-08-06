package model

import play.api.libs.json.Json

case class User (id:Int,firstName:String,lastName:String,mobileNumber:String,emailId:String,password:String,
    isVerified:Boolean,profileImage:Option[String])

object User{
  implicit val userJsonFormat = Json.format[User]
}


//sealed trait Error
//object Error {
//  final case class UserNotFound(userId: Long) extends Error
//  final case class ConnectionError(message: String) extends Error
//}