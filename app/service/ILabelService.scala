package service

import scala.concurrent.Future
import model.Label
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[LabelService])
trait ILabelService {
  def addLabel(label:Label,token:String):Future[String]
}