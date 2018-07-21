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

@Singleton
class UserController @Inject() (userService: IUserService, uservalidation: UserValidation, jwttoken: JwtToken, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /* {
    override def toResult(e: Exception) = super.toResult(e)

  }
*/



  //  def preflight(all: String) = Action {
  //    Ok("").withHeaders("Access-Control-Allow-Origin" -> "*",
  //      "Allow" -> "*",
  //      "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
  //      "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept, Referrer, User-Agent");
  //  }

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

  //    def register() = Action.async { implicit request: Request[AnyContent] =>
  //      var host: String = request.host
  //      println("host" + host)
  //      request.body.asJson.map { json =>
  //        var user: RegisterDto = json.as[RegisterDto]
  //        userService.registerUser(host, user).map {
  //          future => Ok(future)
  //        }.recover {
  //          case exception: Exception => {
  //            exception.printStackTrace()
  //            Conflict("Registration failed..........")
  //          }
  //        }
  //      }.getOrElse(Future {
  //        BadRequest("Registration Failed..!!")
  //      })
  //    }

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
            var token = jwttoken.generateToken(user.id)
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

