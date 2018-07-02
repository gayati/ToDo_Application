package model

import play.api.libs.json.Json

case class Note (noteId:Int,title:String,description:String)

object Note{
  implicit val Note =Json.format[Note] 
}