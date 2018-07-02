package utilities

import javax.inject.Singleton
import javax.inject.Inject
import play.api.cache.redis.CacheAsyncApi
import play.api.cache.redis.connector.RedisConnector
import scala.concurrent.Future

@Singleton
class RedisCache @Inject() (cache: CacheAsyncApi) {

  def saveToken(Id: String, token: String) = {
    cache.set(Id, token)
  }

  def findToken(Id: String): Future[Option[String]] = {
    val token = cache.get[String](Id)
    token
  }

  def deleteToken(Id: String) = {
    val token = cache.remove(Id)
  }
}
 
