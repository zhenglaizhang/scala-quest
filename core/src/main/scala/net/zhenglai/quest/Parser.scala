package net.zhenglai.quest

import cats.syntax.either._

object Parser {

  def parseInt(str: String): Either[String, Int] = {
    Either.catchOnly[NumberFormatException](str.toInt)
      .leftMap(_ => s"Couldn't read $str")
  }

}
