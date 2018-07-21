package utilities

import javax.inject.Singleton
import org.apache.commons.mail._
import play.libs
import play._
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class MailSender @Inject() (configuration: play.api.Configuration)(implicit executionContext: ExecutionContext) {
  
  def sendMail(to: String, subject: String, message: String): Future[Boolean] = {
    Future{
    try {
      val simpleEmail = new SimpleEmail()
      simpleEmail.setFrom(configuration.underlying.getString("smtp.user"))
      simpleEmail.setMsg(message)
      simpleEmail.setSubject(subject)
      simpleEmail.setAuthenticator(new DefaultAuthenticator(configuration.underlying.getString("smtp.user"), configuration.underlying.getString("smtp.password")));
      simpleEmail.setHostName(configuration.underlying.getString("smtp.host"))
      simpleEmail.setSmtpPort(configuration.underlying.getInt("smtp.port"))
      simpleEmail.setSSLOnConnect(configuration.underlying.getBoolean("smtp.starttls"))
      simpleEmail.addTo(to)
      simpleEmail.send()
      print("Email is succesfully sent")
      true
    } catch {
      case e: Exception => e.printStackTrace()
      false
    }
  }
  }
}