package model

import play.api.libs.json.Json

case class User (id:Int,username:String,emailId:String,password:String,isVerified:Boolean)

//object User{
//  implicit val userJsonFormat = Json.format[User]
//}