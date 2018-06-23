package service

import model.{RegisterDto,User}
import scala.concurrent.Future
import com.google.inject.ImplementedBy
import model.LoginDto

@ImplementedBy(classOf[UserService])
trait IUserService {
  def registerUser(user: RegisterDto): Future[String]
  def isUserExist(email: String): Future[Option[User]]
  def loginUser(user:LoginDto):Future[String]
}