package model

import play.api.libs.json.Json

case class Note (noteId:Int,title:String,description:String,isArchived:Boolean,isPinned:Boolean,isTrashed:Boolean,createdBy:Int)

object Note{
  implicit val Note =Json.format[Note] 
}

