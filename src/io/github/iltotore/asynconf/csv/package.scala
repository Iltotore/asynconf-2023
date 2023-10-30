package io.github.iltotore.asynconf.csv

import scala.util.boundary
import scala.util.boundary.break

def readCSV[A](content: String, separator: String = ",")(using decoder: RowDecoder[A]): Either[CSVError, List[A]] =
  val lines = content.split("\n").toList

  boundary[Either[CSVError, List[A]]]:
    val rows =
      for line <- lines yield
        decoder.decode(line.split(",").toList) match
          case Left(error) => break(Left(error))
          case Right(value) => value

    Right(rows)