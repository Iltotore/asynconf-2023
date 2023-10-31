package io.github.iltotore.asynconf.csv

import scala.util.boundary
import scala.util.boundary.break

/**
  * Read CSV content.
  *
  * @param content the text to decode
  * @param separator the column separator (regex)
  * @param decoder the row decoder producing [[A]] values.
  * @return the list of all decoded [[A]] rows or a [[CSVError]] if it failed
  */
def readCSV[A](content: String, separator: String = ",")(using decoder: RowDecoder[A]): Either[CSVError, List[A]] =
  val lines = content.split("\n").toList

  boundary[Either[CSVError, List[A]]]:
    val rows =
      for line <- lines yield
        decoder.decode(line.split(separator).toList) match
          case Left(error) => break(Left(error))
          case Right(value) => value

    Right(rows)