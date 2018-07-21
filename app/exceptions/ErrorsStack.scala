package exceptions

import play.api.mvc.Result
import play.api.mvc.Results

trait ErrorsStack {
  
  case class DefaultError() extends Exception("Something went wrong")

  
  def toResult(e: Exception): Result = e match {
    case DefaultError() => Results.InternalServerError(e.getMessage)
  }
}