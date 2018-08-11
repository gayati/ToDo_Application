package controllers

import javax.inject.Inject
import service.ICollaberatorService
import scala.concurrent.ExecutionContext
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import javax.inject.Singleton
import play.api.mvc.Request
import play.api.mvc.AnyContent
import scala.concurrent.Future
import model.CollaberatorDto

@Singleton
class CollaberatorCtrl @Inject() (collaberatorService: ICollaberatorService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  
  def addCollaberator() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var collaberator: CollaberatorDto = json.as[CollaberatorDto]
      collaberatorService.addCollaberator(collaberator) map { addFuture =>
        Ok(addFuture)
      }
    }.getOrElse(Future {
      BadRequest("Registration Failed..!!")
    })
  }
  
}