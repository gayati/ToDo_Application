package service

import scala.concurrent.Future
import model.Label
import com.google.inject.ImplementedBy
import model.NoteLabel

@ImplementedBy(classOf[LabelService])
trait ILabelService {
  def addLabel(label:Label,token:String):Future[String]
  def getLabels(token:String):Future[Seq[Label]]
    def deleteLabel(labelId:Int,token:String):Future[String]
  def updateLabel(labelId:Int,token:String,label:Label):Future[String]

}