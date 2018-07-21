package dao

import javax.inject.Singleton
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import model.Label
import scala.concurrent.Future


@Singleton
class LabelDao @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends ILabelDao {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class LabelTabel(tag: Tag) extends Table[Label](tag, "Label") {

    def labelId = column[Int]("label_id", O.PrimaryKey, O.AutoInc)

    def labelTitle = column[String]("label_title")

    def userId = column[Int]("user_id")

    override def * = (labelId, labelTitle, userId) <> ((Label.apply _).tupled, Label.unapply)
  }

  private val labels = TableQuery[LabelTabel]
  
  override def addLabel(label:Label):Future[Int]={
      val action = ((labels returning labels.map(_.labelId)) += label)
    db.run(action) map { Id => Id }
  }
}