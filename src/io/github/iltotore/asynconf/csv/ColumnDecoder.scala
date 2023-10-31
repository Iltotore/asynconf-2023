package io.github.iltotore.asynconf.csv

import io.github.iltotore.iron.*

/**
  * The decoder of a column in a row.
  *
  * @tparam A the output type of the decoding
  */
trait ColumnDecoder[+A]:

  /**
    * Decode the given input.
    *
    * @param input the text to decode
    * @return a result of type [[A]] or a [[CSVError]] if the decoding fails
    */
  def decode(input: String): Either[CSVError, A]

  /**
    * Map this decoder's output.
    *
    * @tparam B the new output type
    * @param f the function to apply to the output
    * @return a new decoder applying [[f]] to this decoder's input
    */
  def map[B](f: A => B): ColumnDecoder[B] = (input: String) => decode(input).map(f)

  /**
    * Map this decoder's output or fail.
    *
    * @tparam B the new output type
    * @param f the function to apply to the output, which can either succeed or fail
    * @return a new decoder applying [[f]] to this decoder's input
    */
  def mapEither[B](f: A => Either[CSVError, B]): ColumnDecoder[B] = (input: String) => decode(input).flatMap(f)

object ColumnDecoder:

  /**
    * [[String]] decoder.
    */
  given string: ColumnDecoder[String] = input => Right(input)

  /**
    * [[Int]] decoder.
    */
  given int: ColumnDecoder[Int] = value => value.toIntOption.toRight(CSVError.InvalidValue(value, "Invalid int"))

  /**
    * [[Double]] decoder.
    */
  given double: ColumnDecoder[Double] = value => value.toDoubleOption.toRight(CSVError.InvalidValue(value, "Invalid double"))

  /**
    * Refined type decoder.
    *
    * @tparam A the base type
    * @tparam C the type constraint
    * @param baseDecoder the decoder of [[A]]
    * @param constraint the constraint instance to validate [[baseDecoder]]'s output
    */
  inline given ironType[A, C](using inline baseDecoder: ColumnDecoder[A], inline constraint: Constraint[A, C]): ColumnDecoder[A :| C] = (input: String) =>
    baseDecoder
      .decode(input)
      .flatMap(_.refineEither[C].left.map(msg => CSVError.InvalidValue(input, msg)))

  /**
    * New type decoder.
    *
    * @tparam T the new type
    * @param mirror the metadata of [[T]]
    * @param decoder the decoder of the underlying refined type
    */
  inline given newType[T](using mirror: RefinedTypeOps.Mirror[T], decoder: ColumnDecoder[mirror.IronType]): ColumnDecoder[T] =
    decoder.asInstanceOf[ColumnDecoder[T]]