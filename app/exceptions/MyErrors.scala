package exceptions

import play.api.mvc.Result
import play.api.mvc.Results
import model.User

class MyErrors extends ErrorsStack {

  case class UserAlreadyExists(userName: String) extends Exception("user  already exists")

  
  override def toResult(e: Exception): Result = e match {
    case UserAlreadyExists(userName: String) => Results.BadRequest(e.getMessage)
    case _                            => super.toResult(e)
  }
  


  //   trait Error { def getMessage: String }
  //   final case class UserAlreadyExists(user:User) extends Exception("user  already exists")
  //  final case class UserAlreadyExistsException(userId: String) extends Throwable (s"user $userId not found") with Error

}
