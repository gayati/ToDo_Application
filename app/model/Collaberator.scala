package model

import play.api.libs.json.Json

case class Collaberator(collaberatorId:Int,sharedBy:Int,sharedTo:Int,noteId:Int)

object Collaberator{
  implicit val Collaberator = Json.format[Collaberator]
}