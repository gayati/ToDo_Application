package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import model.Label
import service.ILabelService
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json

@Singleton
class LabelController @Inject() (labelService: ILabelService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addLabel() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var label: Label = json.as[Label]
      labelService.addLabel(label, token) map { addLabelFuture =>
        addLabelFuture match {
          case "CreateSuccess"    => Ok("label created successfully.........")
          case "CreateNotSuccess" => Conflict("Label note created..........")
        }
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  def getLabels() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    labelService.getLabels(token) map { labels =>
      labels
      println(labels)
      Ok(Json.toJson(labels))
    }
  }

  def deleteLabel(labelId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    labelService.deleteLabel(labelId, token) map { deleteFuture =>
      deleteFuture match {
        case "DeleteSuccess"    => Ok("Delete success..........")
        case "DeleteNotSuccess" => Conflict("Delete failure.........")
      }
    }
  }

  def updateLabel(labelId: Int) = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
    request.body.asJson.map { json =>
      var label: Label = json.as[Label]
      labelService.updateLabel(labelId, token, label) map { updateFuture =>
        updateFuture match {
          case "updateSuccess"    => Ok("Update success..........")
          case "updateNotSuccess" => Conflict("Update failure.........")
        }
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })

  }

}
