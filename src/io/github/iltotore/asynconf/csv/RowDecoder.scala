package io.github.iltotore.asynconf.csv

import scala.deriving.Mirror

trait RowDecoder[+A]:

  def decode(input: List[String]): Either[CSVError, A]

  def map[B](f: A => B): RowDecoder[B] = (input: List[String]) => decode(input).map(f)

  def mapEither[B](f: A => Either[CSVError, B]): RowDecoder[B] = (input: List[String]) => decode(input).flatMap(f)

object RowDecoder:

  given RowDecoder[EmptyTuple] =
    case Nil => Right(EmptyTuple)
    case _ => Left(CSVError.InvalidColumnCount)

  given [A, T <: Tuple](using headDecoder: ColumnDecoder[A], tailDecoder: RowDecoder[T]): RowDecoder[A *: T] =
    case head :: tail =>
      for
        decodedHead <- headDecoder.decode(head)
        decodedTail <- tailDecoder.decode(tail)
      yield
        decodedHead *: decodedTail

    case Nil => Left(CSVError.InvalidColumnCount)

  def fromTuple[A, T <: Tuple](f: T => A)(using decoder: RowDecoder[T]): RowDecoder[A] =
    decoder.map(f)

  inline def derived[T](using mirror: Mirror.ProductOf[T], fieldDecoder: RowDecoder[mirror.MirroredElemTypes]): RowDecoder[T] =
    fromTuple[T, mirror.MirroredElemTypes](mirror.fromTuple)
