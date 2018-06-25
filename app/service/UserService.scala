package service

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import model.{ User, RegisterDto }
import scala.concurrent.Future
import javax.inject.Singleton
import utilities.UserValidation
import dao.IUserDao
import model.LoginDto
import org.mindrot.jbcrypt.BCrypt

@Singleton
class UserService @Inject() (validation: UserValidation, userDao: IUserDao)(implicit ec: ExecutionContext) extends IUserService {

  override def registerUser(user: RegisterDto): Future[String] = {
    val user1 = User(0, user.username, user.emailId, user.password)
    userDao.register(user1)
    Future { "Successfully registered" }
    //    println(user.toString() + "service hash")
    //    if ((validation.emailValidate(user.emailId))) {
    //      userDao.register(user).map({
    //        Future => "Success.."
    //      }).recover {
    //        case e: Exception => e.getMessage
    //      }
    //    } else {
    //      Future { "Please enter valid fields" }
    //    }
  }

  override def isUserExist(email: String): Future[Option[User]] = {
    //println(User.toString() + "isExist")
    userDao.isExist(email)
  }

  override def loginUser(loginDto: LoginDto): Future[String] = {
    var tempUser: User = User(0, "", loginDto.emailId, "")
    userDao.login(tempUser).map { loginFuture =>
      if (!(loginFuture.equals(None))) {
        tempUser = loginFuture.get
        if ((BCrypt.checkpw(loginDto.password, tempUser.password))) {
          "Login Success"
        } else {
          "Login Failed"
        }
      } else {
        "Login Failed"
      }
    }
  }

}