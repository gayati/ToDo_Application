package dao

import com.google.inject.ImplementedBy
import scala.concurrent.Future
import model.Label


@ImplementedBy(classOf[LabelDao])
trait ILabelDao {
  def addLabel(label:Label):Future[Int]
}