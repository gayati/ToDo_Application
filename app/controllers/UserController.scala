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

@Singleton
class UserController @Inject() (userService: IUserService, uservalidation: UserValidation, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def register() = Action.async { implicit request: Request[AnyContent] =>

    // var host:String = request.

    // var result: Result = null

    request.body.asJson match {
      case Some(json) => {
        var user: RegisterDto = json.as[RegisterDto]
        //val b = uservalidation.emailValidate(user.emailId)
        println(uservalidation.emailValidate(user.emailId))
        if ((uservalidation.emailValidate(user.emailId)) && (uservalidation.passwordValidate(user.password))) {
          var passwordHash: String = BCrypt.hashpw(user.password, BCrypt.gensalt());
          var user1 = RegisterDto(user.username, user.emailId, passwordHash)
          println(user1.toString() + "hassh")
          userService.isUserExist(user1.emailId) map {
            userFuture =>
              userFuture match {
                case Some(user1) => {
                  BadRequest("User already exists")
                }
                case None => {
                  print(userFuture.toString() + "return")
                  var string: Future[Result] = userService.registerUser(user1) map {
                    registrationFuture =>
                      Ok(registrationFuture)
                  }
                  Ok("User registration successful")
                }
              }
            //            println(result.body)
            //            result
          }

        } else {
          Future { Ok("Please enter valid fields") }
        }
      }
      case None => {
        Future(BadRequest("Something went wrong in json parsing...."))
      }
    }

  }

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
 

}
 

//    request.body.asJson.map { json =>
//      var user: User = json.as[User]
//    if (uservalidation.emailValidate(user.emailId)) {
//      userService.isUserExist(user.emailId).map { userFuture =>
//        if (userFuture.equals(None)) {
//          userService.registerUser(user).map {
//            Ok("registration success")
//          }
//        } else {
//          Ok("User Already exist")
//        }
//
//      } 
//    }
//    else{
//          Future { Ok("Please enter valid fields") }
//        }
//    }.getOrElse(Future {
//      BadRequest("Something went wrong in json parsing....")
//    })
//  }
//}