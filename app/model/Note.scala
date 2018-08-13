package model

import play.api.libs.json.Json
import java.util.Date

case class Note (noteId:Int,title:String,description:String,createdDate:Date,updatedDate:Date,color:String,
    isArchived:Boolean,isPinned:Boolean,isTrashed:Boolean,createdBy:Int,reminder:Option[Date],
    remindertime:Option[String],image:Option[String],collaberator:Int)

object Note{
  implicit val Note =Json.format[Note] 
}

