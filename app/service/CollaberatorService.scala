package service

import javax.inject.Singleton
import javax.inject.Inject
import dao.UserDao
import utilities.JwtToken
import scala.concurrent.ExecutionContext
import model.Collaberator
import scala.concurrent.Future
import model.CollaberatorDto

@Singleton
class CollaberatorService @Inject() (userDao: UserDao)(implicit ec: ExecutionContext) extends ICollaberatorService {  
  
  override def addCollaberator(collaberator:CollaberatorDto):Future[String]={
  userDao.getUserByemail(collaberator.sharedTo) map{ UserFuture =>
    UserFuture
    var sharedUser = UserFuture.get
      var collaberator1 = Collaberator(0,collaberator.sharedBy,sharedUser.id,collaberator.noteId)
    userDao.addCollaberator(collaberator1) map { addsuccess =>
      addsuccess
    }
     "addsuccess"
  }
  
  }
  
  override def getCollaberator(collaberatorId:Int):Future[Seq[Collaberator]]={
    userDao.getCollaberator(collaberatorId) map { getCollaberatoFuture =>
      getCollaberatoFuture 
    }
  }

}
