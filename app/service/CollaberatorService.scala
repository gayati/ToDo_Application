package service

import javax.inject.Singleton
import javax.inject.Inject
import dao.UserDao
import utilities.JwtToken
import scala.concurrent.ExecutionContext
import model.Collaberator
import scala.concurrent.Future

@Singleton
class CollaberatorService @Inject() (userDao: UserDao,jwtToken:JwtToken)(implicit ec: ExecutionContext) extends ICollaberatorService {  
  
  override def addCollaberator(collaberator:Collaberator):Future[String]={
    userDao.addCollaberator(collaberator) map { addsuccess =>
      addsuccess
       "addsuccess"
    }
  }
}
