package net.zhenglai.quest

import cats.data.Reader
import cats.syntax.applicative._ // for `pure`

case class Db(
  usernames: Map[Int, String],
  passwords: Map[String, String]
)

object AuthUser {

  // type alias fixes the Db type but leaves the result type flexible
  // consumes the Db as input
  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] = {
    Reader(db => db.usernames.get(userId))
  }

  def checkPassword(username: String, password: String): DbReader[Boolean] = {
    Reader(db => db.passwords.get(username).contains(password))
  }

  def checkLogin(userId: Int, password: String): DbReader[Boolean] = {
    for {
      username <- findUsername(userId)
      passwordOk <- username.map { un =>
        checkPassword(un, password)
      }.getOrElse(false.pure[DbReader])
    } yield passwordOk
  }
}
