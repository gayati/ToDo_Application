package dao

import model.User
import scala.concurrent.Future
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[UserDao])
trait IUserDao {
  def register(user: User): Future[Int]
  def isExist(email: String): Future[Option[User]]
  def login(user: User): Future[Option[User]]
  def getUserById(id:Int):Future[Option[User]]
  def getUsetByEmail(email: String): Future[Option[User]]
  def update(user:User):Future[Int]
  def getUserByemail(emailid:String):Future[Option[User]]
}