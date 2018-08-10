package model

import play.api.libs.json.Json

case class Collaberator(collaberatorId:Int,sharedBy:String,sharedTo:String,noteId:Int)

object Collaberator{
  implicit val Collaberator = Json.format[Collaberator]
}