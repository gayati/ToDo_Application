package service

import model.Collaberator
import scala.concurrent.Future

trait ICollaberatorService {
  def addCollaberator(collaberator:Collaberator):Future[String]
}