package model

import play.api.libs.json.Json
import java.sql.Date

case class Note (noteId:Int,title:String,description:String,createdDate:Date,updatedDate:Date,color:String,
    isArchived:Boolean,isPinned:Boolean,isTrashed:Boolean,createdBy:Int)

object Note{
  implicit val Note =Json.format[Note] 
}

