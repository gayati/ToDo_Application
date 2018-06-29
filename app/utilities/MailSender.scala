package utilities

import javax.inject.Singleton
import org.apache.commons.mail._
import play.libs
import play._
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

@Singleton
class MailSender @Inject() (implicit executionContext: ExecutionContext) {
  
  def sendMail(to: String, subject: String, message: String) = {
    try {
      val simpleEmail = new SimpleEmail()
      simpleEmail.setFrom("playscala2018@gmail.com")
      simpleEmail.setMsg(message)
      simpleEmail.setSubject(subject)
      simpleEmail.setAuthenticator(new DefaultAuthenticator("playscala2018@gmail.com", "scala@2018"));
      simpleEmail.setHostName("smtp.gmail.com");
      simpleEmail.setSmtpPort(587);
      simpleEmail.setSSLOnConnect(true);
      simpleEmail.addTo(to)
      simpleEmail.send()
      print("Email is succesfully sent")
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}