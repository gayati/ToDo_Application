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
import model.NoteDto

@Singleton
class UserController @Inject() (userService: IUserService, uservalidation: UserValidation, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def register() = Action.async { implicit request: Request[AnyContent] =>
    var host: String = request.host
    println("host" + host)
    var url: String = request.uri
    println("url" + url)
    request.body.asJson.map { json =>
      var user: RegisterDto = json.as[RegisterDto]
      userService.registerUser(url, host, user).map {
        future => Ok(future)
      }.recover {
        case exception: Exception => {
          Conflict("Registration failed..!!")
        }
      }
    }.getOrElse(Future {
      BadRequest("Registration Failed..!!")
    })

  }

  def isActivated(token: String) = Action.async { implicit request: Request[AnyContent] =>
    println("token in controller: " + token)
    userService.activateUser(token).map({
      future => Ok(future)
    })
  }

  def forgotPassword() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var passwordDto: ForgotPasswordDto = json.as[ForgotPasswordDto]
      userService.forgotUserPassword(passwordDto).map { future => Ok(future)
      }
    }.getOrElse(Future {
      BadRequest("")
    })
  }

  def resetPassword(token: String) = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var passwordDto: PasswordDto = json.as[PasswordDto]
      userService.resetUserPassword(token, passwordDto).map { future =>
        Ok(future)
      }
    }.getOrElse(Future {
      BadRequest("")
    })
  }
  //val b = uservalidation.emailValidate(user.emailId)
  //        println(uservalidation.emailValidate(user.emailId))
  //        if ((uservalidation.emailValidate(user.emailId)) && (uservalidation.passwordValidate(user.password))) {
  //          var passwordHash: String = BCrypt.hashpw(user.password, BCrypt.gensalt());
  //          var user1 = RegisterDto(user.username, user.emailId, passwordHash)
  //          println(user1.toString() + "hassh")
  //          userService.isUserExist(user1.emailId) map {
  //            userFuture =>
  //              userFuture match {
  //                case Some(user1) => {
  //                  BadRequest("User already exists")
  //                }
  //                case None => {
  //                  print(userFuture.toString() + "return")
  //                  var string: Future[Result] = userService.registerUser(user1) map {
  //                    registrationFuture =>
  //                      Ok(registrationFuture)
  //                  }
  //                  Ok("User registration successful")
  //                }
  //              }
  //            //            println(result.body)
  //            //            result
  //          }
  //
  //        } else {
  //          Future { Ok("Please enter valid fields") }
  //        }
  //      }
  //      case None => {
  //        Future(BadRequest("Something went wrong in json parsing...."))
  //      }
  //    }
  //
  //  }

  def login() = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      var userLogin: LoginDto = json.as[LoginDto]
      userService.loginUser(userLogin).map {
        loginFuture => Ok(loginFuture)
      }
    }.getOrElse(Future {
      BadRequest("User has made a bad request")
    })
  }
  
//  
}
 

