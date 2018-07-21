package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import model.Label
import service.ILabelService
import scala.concurrent.Future
import scala.concurrent.ExecutionContext


@Singleton
class LabelController @Inject()(labelService:ILabelService,cc: ControllerComponents) (implicit ec: ExecutionContext) extends AbstractController(cc) {

 
  def addLabel() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var label: Label = json.as[Label]
      labelService.addLabel(label,token)map { addLabelFuture =>
        Ok(addLabelFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  
}
