package model

import play.api.libs.json.Json

case class Label(labelId:Int,label:String,userId:Int)

object Label{
  implicit val Label = Json.format[Label] 
}