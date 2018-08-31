package controllers

import javax.inject.Inject
import play.api._
import play.api.mvc._
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import model.User
import service.IUserService
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import javax.inject.Singleton
import org.mindrot.jbcrypt.BCrypt
import utilities.UserValidation
import model.LoginDto
import model.RegisterDto
import model.ForgotPasswordDto
import model.PasswordDto
import exceptions.MyErrors
import java.nio.charset.StandardCharsets.UTF_8
import play.api.libs._
import scala.concurrent.duration._
import scala.concurrent.Await
import utilities.JwtToken
import play.api.libs.json.Json

@Singleton
class UserController @Inject() (userService: IUserService, uservalidation: UserValidation, jwttoken: JwtToken, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def register() = Action.async { implicit request: Request[AnyContent] =>

    var host: String = request.host
    println("host" + host)
    var url: String = request.headers.get("origin").get
    println(url + "url")
    request.body.asJson.map { json =>
      var user: RegisterDto = json.as[RegisterDto]
      userService.registerUser(host, url, user).map { userFuture =>
        userFuture match {
          case "NotExist" => Ok("User registered successfully")
          case "Exist" =>
            //            val res = errors.toResult(errors.UserAlreadyExists(user.emailId))
            //            val body = res.header.status.toInt
            // val body = new String(Await.result(res.body.run(Iteratee.consume()), 5 seconds), UTF_8)
            Conflict("User Alredy Exist")
        }
      }.recover {
        case error =>
          //errors.toResult(errors.DefaultError())
          Conflict("Exception............")
      }
    }.getOrElse(Future {
      BadRequest("Registration Failed..!!")
    })
  }

  def isActivated(token: String) = Action.async { implicit request: Request[AnyContent] =>
    println("token in controller: " + token)
    userService.activateUser(token).map({
      future =>
        future match {
          case "Activated"    => Ok("Activation Successs.....")
          case "Notactivated" => Conflict("Failed......")
        }
    }).recover {
      case exception: Exception => {
        exception.printStackTrace()
        Conflict("Activation failed..........")
      }
    }
  }

  def forgotPassword() = Action.async { implicit request: Request[AnyContent] =>
    var host = request.host
    var url: String = request.headers.get("origin").get
    request.body.asJson.map { json =>
      var passwordDto: ForgotPasswordDto = json.as[ForgotPasswordDto]
      userService.forgotUserPassword(host, url, passwordDto) map { future =>
        future match {
          case "UserPresent"    => Ok("User Present");
          case "UserNotpresent" => Conflict("User Not Present")
        }
      }
    }.getOrElse(Future {
      BadRequest("")
    })
  }

  def resetPassword(token: String) = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var passwordDto: PasswordDto = json.as[PasswordDto]
      userService.resetUserPassword(token, passwordDto).map { future =>
        future match {
          case "Reset" => Ok("Password reset success.........");
          case "Not"   => Conflict("Password reset fail.....")
        }
      }.recover {
        case exception: Exception => {
          exception.printStackTrace()
          Conflict("Exception Ocurred..........")
        }
      }
    }.getOrElse(Future {
      BadRequest("")
    })
  }

  def login() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var userLogin: LoginDto = json.as[LoginDto]
      userService.loginUser(userLogin).map { loginFuture =>
        loginFuture match {
          case Some(user) =>
            var token = jwttoken.generateLoginToken(user.id, user.firstName, user.lastName, user.emailId)
            Ok(token).withHeaders("Headers" -> token)
          case None => Conflict("User not registered.....")
        }
      }.recover {
        case exception: Exception => {
          exception.printStackTrace()
          Conflict("Exception Ocurred..........")
        }
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }

  def updateUser() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var user = json.as[User]
      userService.updateUser(user).map { updateUserFuture =>
        Ok(updateUserFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }
  
   def getUser() = Action.async { implicit request: Request[AnyContent] =>
    var token = request.headers.get("Headers").get
        val userId = jwttoken.getTokenId(token)

    userService.getUser(userId) map { user =>
      user
      println(user)
      Ok(Json.toJson(user))
    }
  }
   
   def getAllUsers =Action.async { implicit request: Request[AnyContent] =>
   userService.getAllUsers map { users =>
     users
      Ok(Json.toJson(users))
   }
   }
   
    def getOwner(userId:Int) = Action.async { implicit request: Request[AnyContent] =>
    userService.getUser(userId) map { user =>
      user
      println(user)
      Ok(Json.toJson(user))
    }
  }
}
 
  

