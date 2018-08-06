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
import play.api.mvc.ResponseHeader
//import exceptions.MyErrors

@Singleton
class UserService @Inject() (uservalidation: UserValidation, userDao: IUserDao, jwttoken: JwtToken, mailsender: MailSender, rediscache: RedisCache)(implicit ec: ExecutionContext) extends IUserService {

  //  override def registerUser(host: String, registerDto: RegisterDto): Future[String] = {
  //    if (uservalidation.emailValidate(registerDto.emailId) &&
  //      uservalidation.passwordValidate(registerDto.password)) {
  //      var passwordHash: String = BCrypt.hashpw(registerDto.password, BCrypt.gensalt());
  //      var user1 = User(0, registerDto.firstName,registerDto.lastName,registerDto.mobileNumber, registerDto.emailId, passwordHash,false)
  //      println(user1.toString() + "hassh")
  //      userDao.isExist(user1.emailId) map {
  //        userFuture =>
  //          println(userFuture + "UserFuture")
  //          userFuture match {
  //            case Some(user1) => {
  //              "User already exists"
  //            }
  //            case None => {
  //              print(userFuture.toString() + "return")
  //              userDao.register(user1) map {
  //                userIdFuture =>
  //                  var id = userIdFuture
  //                  val token = jwttoken.generateToken(id)
  //                  println(token)
  //                  var emailTo: String = user1.emailId
  //                  val subject: String = "Link to activate your account"
  //                  val message: String = "Please visit the given link to activate your account \n http://" + host + "/activateuser/" + token;
  //                  mailsender.sendMail(emailTo, subject, message)
  //                  rediscache.saveToken(Integer.toString(id), token)
  //              }
  //              "User registration successful"
  //            }
  //          }
  //      }
  //    } else {
  //      Future { "Please enter valid fields" }
  //    }
  //  }

  override def registerUser(host: String, url: String, registerDto: RegisterDto): Future[String] = {

    var passwordHash: String = BCrypt.hashpw(registerDto.password, BCrypt.gensalt());
    var user1 = User(0, registerDto.firstName, registerDto.lastName, registerDto.mobileNumber, registerDto.emailId, passwordHash, false,None)
    println(user1.toString() + "hassh")
    userDao.isExist(user1.emailId) map {
      userFuture =>
        userFuture match {
          case Some(user1) => {
            "Exist"
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
                val message: String = "Please visit the given link to activate your account \n" + url + "#!/activateUser?token=" + token;
                // val message: String = "Please visit the given link to activate your account \n http://" + host + "/activateuser/" + token;
                mailsender.sendMail(emailTo, subject, message)
                rediscache.saveToken(Integer.toString(id), token)
            }
            "NotExist"
          }
        }
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

//  def getUser(tokenId: String) = {
//    val id: Int = jwttoken.getTokenId(tokenId)
//    print(id)
//    userDao.getUserById(id).map({
//      userFuture => userFuture
//    })
//  }

  override def activateUser(tokenId: String): Future[String] = {
    val id: Int = jwttoken.getTokenId(tokenId)
    rediscache.findToken(Integer.toString(id)).map { tokefuture =>
      val token = tokefuture.get
      println("return token" + token)
      println("tokenId: " + tokenId)
      if (token.equals(tokenId)) {
        userDao.getUserById(id).map({ userFuture =>
          var user = userFuture.get
          var verified = user.isVerified
          verified = true
          user = User(user.id, user.firstName, user.lastName, user.mobileNumber, user.emailId, user.password, verified,None)
          println(user.toString())
          userDao.update(user).map({ updateFuture =>
            updateFuture
            println(updateFuture)
            "Activated"
          })
          "Activated"
        })
        rediscache.deleteToken(id.toString())
        "Activated"
      } else {
        "Notactivated"
      }
    }
  }

  override def loginUser(loginDto: LoginDto): Future[Option[User]] = {
    var tempUser: User = User(0, "", "", "", loginDto.emailId, "", false,None)
    //    if (uservalidation.emailValidate(tempUser.emailId)) {
    userDao.login(tempUser).map { loginFuture =>
      if (!(loginFuture.equals(None))) {
        tempUser = loginFuture.get
        if (tempUser.isVerified == true &&
          (BCrypt.checkpw(loginDto.password, tempUser.password))) {
          var token = jwttoken.generateToken(tempUser.id)
          Some(tempUser)
        } else {
          None
        }
      } else {
        None
      }
    }
    //    } else {
    //      Future { "Please enter valid fields" }
    //    }
  }

  override def forgotUserPassword(host: String, url: String, passwordDto: ForgotPasswordDto): Future[String] = {
    userDao.getUserByemail(passwordDto.emailId).map { userFuture =>
      if (!(userFuture.equals(None))) {
        var user = userFuture.get
        if (user.isVerified == true) {
          val token: String = jwttoken.generateToken(user.id)
          var emailTo: String = user.emailId
          val subject: String = "Link to reset your password"
          val message: String = "Please visit the given link to reset your password  \n" + url + "#!/resetPassword?token=" + token;
          mailsender.sendMail(emailTo, subject, message)
          rediscache.saveToken(user.id.toString(), token)
          "UserPresent"
        } else {
          "UserNotpresent"
        }
        "UserPresent"
      } else {
        "UserNotpresent"
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
          user = User(user.id, user.firstName, user.lastName, user.mobileNumber, user.emailId, passwordHash, user.isVerified,None)
          userDao.update(user).map { updateFuture =>
            updateFuture
            "Reset"
          }
        }
        rediscache.deleteToken(id.toString())
        "Reset"
      } else {
        "Not"
      }
    }
  }
  
  override def updateUser(user:User):Future[String] ={
    userDao.update(user) map { updateUserFuture =>
      updateUserFuture
      "Updated"
    }
  }
  
    override def getUser(token: String): Future[Option[User]] = {
      println("In get user token:......................." + token)
    val userId = jwttoken.getTokenId(token)
    println("In get user................." + userId)
    userDao.getUserById(userId) map { userFuture =>
      if (!(userFuture.equals(None))) {
        userFuture
      } else {
       null
      }
    }
  }
  

}