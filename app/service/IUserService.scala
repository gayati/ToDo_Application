package service

import model.{ RegisterDto, User }
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.LoginDto
import model.ForgotPasswordDto
import model.PasswordDto
import model.NoteDto

@ImplementedBy(classOf[UserService])
trait IUserService {
 def registerUser(host: String,url:String, user: RegisterDto): Future[String]
  
  def loginUser(user: LoginDto): Future[Option[User]]
  def activateUser(token: String): Future[String]
  def forgotUserPassword(host:String,url:String,passwordDto: ForgotPasswordDto): Future[String]
  def resetUserPassword(token: String, passwordDto: PasswordDto): Future[String]
  def updateUser(user:User):Future[String]
  def getUser(userID:Int):Future[Option[User]]
  def getUserByEmail(email:String):Future[Option[User]]
    def getAllUsers :Future[Seq[User]]
}