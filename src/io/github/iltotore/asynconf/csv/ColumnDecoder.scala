package io.github.iltotore.asynconf.csv

import io.github.iltotore.iron.*

trait ColumnDecoder[+A]:

  def decode(input: String): Either[CSVError, A]

  def map[B](f: A => B): ColumnDecoder[B] = (input: String) => decode(input).map(f)

  def mapEither[B](f: A => Either[CSVError, B]): ColumnDecoder[B] = (input: String) => decode(input).flatMap(f)

object ColumnDecoder:

  given string: ColumnDecoder[String] = input => Right(input)

  given int: ColumnDecoder[Int] = value => value.toIntOption.toRight(CSVError.InvalidValue(value, "Invalid int"))

  given double: ColumnDecoder[Double] = value => value.toDoubleOption.toRight(CSVError.InvalidValue(value, "Invalid double"))

  inline given ironType[A, C](using inline baseDecoder: ColumnDecoder[A], inline constraint: Constraint[A, C]): ColumnDecoder[A :| C] = (input: String) =>
    baseDecoder
      .decode(input)
      .flatMap(_.refineEither[C].left.map(msg => CSVError.InvalidValue(input, msg)))

  inline given newType[T](using mirror: RefinedTypeOps.Mirror[T], decoder: ColumnDecoder[mirror.IronType]): ColumnDecoder[T] =
    decoder.asInstanceOf[ColumnDecoder[T]]