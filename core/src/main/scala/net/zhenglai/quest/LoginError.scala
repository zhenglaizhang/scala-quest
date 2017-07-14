package net.zhenglai.quest

sealed trait LoginError extends Product with Serializable

final case class UserNotFound(userName: String) extends LoginError

final case class PasswordIncorrect(userName: String) extends LoginError

case object UnexpectedError extends LoginError

case class User(userName: String, password: String)

object LoginError {
  def handleError(error: LoginError): Unit = error match {
    case UserNotFound(u) => println(s"User not found: $u")
    case PasswordIncorrect(u) => println(s"Password incorrect: $u")
    case UnexpectedError => println("Unexpected error")
  }
}
