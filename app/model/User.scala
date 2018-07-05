package model

import play.api.libs.json.Json

case class User (id:Int,firstName:String,lastName:String,mobileNumber:String,emailId:String,password:String,isVerified:Boolean)

//object User{
//  implicit val userJsonFormat = Json.format[User]
//}