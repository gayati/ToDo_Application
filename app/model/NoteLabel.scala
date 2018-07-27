package model

import play.api.libs.json.Json

case class NoteLabel (noteId: Int, labelId: Int)

object NoteLabel {
  implicit val format = Json.format[NoteLabel]
}