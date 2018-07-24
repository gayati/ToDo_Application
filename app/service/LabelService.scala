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
class LabelService @Inject() (userDao: UserDao, labelDao: LabelDao, jwtToken: JwtToken)(implicit ec: ExecutionContext) extends ILabelService {

  override def addLabel(label: Label, token: String): Future[String] = {
    val id = jwtToken.getTokenId(token)
    userDao.getUserById(id) map { userFuture =>
      if (!(userFuture.equals(None))) {
        val user = userFuture.get
        val label1 = Label(label.labelId, label.label, user.id)
        labelDao.addLabel(label1) map { addLabeFuture =>
          addLabeFuture
          "CreateSuccess"
        }
        "CreateSuccess"
      } else {
        "CreateNotSuccess"
      }
    }
  }

  override def getLabels(token: String): Future[Seq[Label]] = {
    val userId = jwtToken.getTokenId(token)
    labelDao.getLabels(userId) map { labelFuture =>
      if (!(labelFuture.equals(None))) {
        labelFuture
      } else {
        null
      }
    }
  }

  override def deleteLabel(labelId: Int, token: String): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    labelDao.getLabelById(labelId) map { labelFuture =>
      if (!(labelFuture.equals(None))) {
        val label = labelFuture.get
        if (uId == label.userId) {
          labelDao.deleteLabel(labelId) map { deletelabelFuture =>
            deletelabelFuture
            "DeleteSuccess"
          }
        } else {
          "DeleteNotSuccess"
        }
        "DeleteSuccess"
      } else {
        "DeleteNotSuccess"
      }
    }
  }

  override def updateLabel(labelId: Int, token: String,label2:Label): Future[String] = {
    val uId = jwtToken.getTokenId(token)
    labelDao.getLabelById(labelId) map { labelFuture =>
      println("fssssssssssss" + labelFuture)
      if (!(labelFuture.equals(None))) {
        var label = labelFuture.get
        println("Label........" +label)
        if (uId == label.userId) {
          label = Label(label.labelId,label2.label,label.userId)
          labelDao.updateLabel(label) map { updteFuture =>
            updteFuture
            "updateSuccess"
          }
        } else {
          "updateNotSuccess"
        }
        "updateSuccess"
      } else {
        "updateNotSuccess"
      }
    }

  }

}
