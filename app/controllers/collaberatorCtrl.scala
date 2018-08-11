package controllers

import play.api._
import play.api.mvc._
import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.ControllerComponents
import scala.concurrent.ExecutionContext
import play.api.mvc.AbstractController
import play.api.mvc.Request
import play.api.mvc.AnyContent
import model.Collaberator
import service.ICollaberatorService
import scala.concurrent.Future



@Singleton
class collaberatorCtrl @Inject() (collaberatorService: ICollaberatorService,cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

// def addCollaberator() = Action.async { implicit request: Request[AnyContent] =>
//    request.body.asJson.map { json =>
//      var collaberator: Collaberator = json.as[Collaberator]
//      collaberatorService.addCollaberator(collaberator) map { addFuture =>
//        Ok(addFuture)
//      }
//    }.getOrElse(Future {
//      BadRequest("Registration Failed..!!")
//    })
//  }
}
