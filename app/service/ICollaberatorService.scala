package service

import model.Collaberator
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.CollaberatorDto

@ImplementedBy(classOf[CollaberatorService])
trait ICollaberatorService {
  def addCollaberator(collaberator:CollaberatorDto):Future[String]
  def getCollaberator(collaberatorId:Int):Future[Seq[Collaberator]]
}