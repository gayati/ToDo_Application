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
import utilities.JwtToken
import utilities.MailSender
import model.ForgotPasswordDto
import model.PasswordDto
import play.api.cache.redis._
import utilities.RedisCache
import model.NoteDto

@Singleton
class UserService @Inject() (uservalidation: UserValidation, userDao: IUserDao, jwttoken: JwtToken, mailsender: MailSender, rediscache: RedisCache)(implicit ec: ExecutionContext) extends IUserService {

  override def registerUser(url: String, host: String, user: RegisterDto): Future[String] = {
    //val user1 = User(0, user.username, user.emailId, user.password)

    if (uservalidation.emailValidate(user.emailId) &&
      uservalidation.passwordValidate(user.password)) {
      var passwordHash: String = BCrypt.hashpw(user.password, BCrypt.gensalt());
      var user1 = User(0, user.username, user.emailId, passwordHash, false)
      println(user1.toString() + "hassh")
      userDao.isExist(user1.emailId) map {
        userFuture =>
          println(userFuture + "UserFuture")
          userFuture match {
            case Some(user1) => {
              "User already exists"
            }
            case None => {
              print(userFuture.toString() + "return")
              userDao.register(user1) map {
                userIdFuture =>
                  var id = userIdFuture
                  val token = jwttoken.generateToken(id)
                  println(token)
                  var emailTo: String = user1.emailId
                  val subject: String = "Link to activate your account"
                  val message: String = "Please visit the given link to activate your account \n http://" + host + "/activateuser/" + token;
                  mailsender.sendMail(emailTo, subject, message)
                  rediscache.saveToken(Integer.toString(id), token)
              }
              "User registration successful"
            }
          }
      }
    } else {
      Future { "Please enter valid fields" }
    }
  }
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

  //  override def isUserExist(email: String): Future[Option[User]] = {
  //    //println(User.toString() + "isExist")
  //    userDao.isExist(email)
  //  }

  def getUser(tokenId: String) = {
    val id: Int = jwttoken.getTokenId(tokenId)
    print(id)
    userDao.getUserById(id).map({
      userFuture => userFuture
    })
  }

  override def activateUser(tokenId: String): Future[String] = {
    val id: Int = jwttoken.getTokenId(tokenId)
    rediscache.findToken(Integer.toString(id)).map { tokefuture =>
      val token = tokefuture.get
      println("return token" + token)
      println("tokenId: " + tokenId)
      if (token.equals(tokenId)) {
        userDao.getUserById(id).map({ userFuture =>
          var user = userFuture.get
          user = User(user.id, user.username, user.emailId, user.password, true)
          println(user.toString())
          userDao.update(user).map({ updateFuture =>
            updateFuture
            println(updateFuture)
            "User sucessfully verified"
          })
          "User successfully verified"
        })
        rediscache.deleteToken(id.toString())
        "User successfully verified and activated"
      } else {
        "User not activated"
      }
    }
  }

  override def loginUser(loginDto: LoginDto): Future[String] = {
    var tempUser: User = User(0, "", loginDto.emailId, "", false)
    if (uservalidation.emailValidate(tempUser.emailId)) {
      userDao.login(tempUser).map { loginFuture =>
        if (!(loginFuture.equals(None))) {
          tempUser = loginFuture.get
          if ((BCrypt.checkpw(loginDto.password, tempUser.password)) && tempUser.isVerified == true) {
            "Login Success"
          } else {
            "Login Failed"
          }
        } else {
          "User is not registered,Please registered first"
        }
      }
    } else {
      Future { "Please enter valid fields" }
    }
  }

  override def forgotUserPassword(host: String, passwordDto: ForgotPasswordDto): Future[String] = {
    userDao.getUserByemail(passwordDto.emailId).map { userFuture =>
      var user = userFuture.get
      if (user.isVerified == true) {
        val token: String = jwttoken.generateToken(user.id)
        var emailTo: String = user.emailId
        val subject: String = "Link to reset your password"
        val message: String = "Please visit the given link to reset your password \n http://" +
          host + "/resetpassword/" + token;
        mailsender.sendMail(emailTo, subject, message)
        rediscache.saveToken(user.id.toString(), token)
        "User found"
      } else {
        "User not found"
      }
    }
  }

  override def resetUserPassword(tokenId: String, passwordDto: PasswordDto): Future[String] = {
    val id = jwttoken.getTokenId(tokenId)
    rediscache.findToken(Integer.toString(id)).map { tokefuture =>
      val token = tokefuture.get
      if (token.equals(tokenId)) {
        userDao.getUserById(id).map { userFuture =>
          var user = userFuture.get
          var passwordHash: String = BCrypt.hashpw(passwordDto.password, BCrypt.gensalt());
          user = User(user.id, user.username, user.emailId, passwordHash, user.isVerified)
          userDao.update(user).map { updateFuture =>
            updateFuture
            "Password is successfully reset"
          }
        }.recover {
          case e: Exception => (e.printStackTrace())
        }
        rediscache.deleteToken(id.toString())
        "Password is successfully reset"
      } else {
        ""
      }
    }
  }

}