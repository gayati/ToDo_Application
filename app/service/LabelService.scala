package service

import javax.inject.Singleton
import javax.inject.Inject
import utilities.JwtToken
import scala.concurrent.ExecutionContext
import model.Label
import scala.concurrent.Future
import dao.UserDao
import dao.LabelDao


@Singleton
class LabelService @Inject() (userDao:UserDao,labelDao:LabelDao, jwtToken: JwtToken)(implicit ec: ExecutionContext) extends ILabelService {

  override def addLabel(label:Label, token: String): Future[String] = {
        val id = jwtToken.getTokenId(token)
    userDao.getUserById(id) map { userFuture =>
      if (!(userFuture.equals(None))) {
        val user = userFuture.get
        val label1 = Label(0,label.label,label.labelId )
        labelDao.addLabel(label1) map { addLabeFuture =>
          addLabeFuture
          "Label Created successfully"
        }
        "Label Created successfully"
      } else {
        "User not found"
      }
    }
     
    }
  }
