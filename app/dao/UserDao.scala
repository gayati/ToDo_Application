package dao

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import javax.inject.Singleton
import play.api.db.Database
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import model.User
import scala.concurrent.Future
import model.Note

@Singleton
class UserDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends IUserDao {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def mobileNumber = column[String]("mobile_number")

    def email = column[String]("email")

    def passWord = column[String]("password")

    def isVerified = column[Boolean]("isverified")

    override def * = (id, firstName, lastName, mobileNumber, email, passWord, isVerified) <> ((User.apply _).tupled, User.unapply)

  }

  private val users = TableQuery[UserTable]

  override def register(user: User): Future[Int] = {
    val action = ((users returning users.map(_.id)) += user)
    db.run(action).map(id => id)
  }

  override def isExist(email: String): Future[Option[User]] = {
    db.run(users.filter(_.email === email).result.headOption)

  }

  override def login(user: User): Future[Option[User]] = {
    db.run((users.filter(_.email === user.emailId)).result.headOption)
  }

  override def getUsetByEmail(email: String): Future[Option[User]] = {
    db.run((users.filter(_.email === email).result.headOption))
  }

  def getUserById(id: Int): Future[Option[User]] = {
    db.run((users.filter(_.id === id)).result.headOption)
  }
  override def update(user: User): Future[Int] = {
    println(user.toString() + "user updated")
    db.run(users.filter(_.id === user.id).update(user))
  }

  override def getUserByemail(emailid: String): Future[Option[User]] = {
    db.run(users.filter(_.email === emailid).result.headOption)
  }

  //  override def createNote(note:Note):Future[String]={
  //
  //  }

}