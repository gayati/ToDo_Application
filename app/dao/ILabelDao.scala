package dao

import com.google.inject.ImplementedBy
import scala.concurrent.Future
import model.Label
import model.NoteLabel


@ImplementedBy(classOf[LabelDao])
trait ILabelDao {
  def addLabel(label:Label):Future[Int]
  def getLabels(userId:Int):Future[Seq[Label]]
  def getLabelById(labelId:Int):Future[Option[Label]]
  def deleteLabel(labelId:Int):Future[Int]
  def updateLabel(label:Label):Future[Int]
 // def addNoteLabel(noteLabel:NoteLabel):Future[Int ]
}