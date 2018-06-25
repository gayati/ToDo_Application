package utilities
import javax.inject.Singleton
import java.util.regex.Pattern

@Singleton
class UserValidation {

  var emailPattern: Pattern = Pattern.compile("^[a-z0-9+_.-]+@{1}[a-z](.+){1}[a-z]{2,}");
    var passwordPattern: Pattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_]{8,}");
  def emailValidate(email: String): Boolean = {

    if (emailPattern.matcher(email).matches() && !email.equals("")) {
      true
    } else {
      false
    }
  }


  def passwordValidate(password: String): Boolean = {

    if (passwordPattern.matcher(password).matches() && !password.equals("")) {
      true
    } else {
      false
    }
  }

}