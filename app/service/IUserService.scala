package service

import model.{RegisterDto,User}
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.LoginDto
import model.ForgotPasswordDto
import model.PasswordDto

@ImplementedBy(classOf[UserService])
trait IUserService {
  def registerUser(url:String,host:String,user: RegisterDto): Future[String]
 // def isUserExist(email: String): Future[Option[User]]
  def loginUser(user:LoginDto):Future[String]
  def activateUser(token:String):Future[String]
  def forgotUserPassword(passwordDto:ForgotPasswordDto):Future[String]
  def resetUserPassword(token:String,passwordDto:PasswordDto):Future[String]
}