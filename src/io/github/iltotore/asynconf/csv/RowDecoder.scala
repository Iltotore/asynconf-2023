package io.github.iltotore.asynconf.csv

import scala.deriving.Mirror

/**
  * The decoder of a row in a CSV file.
  * 
  * @tparam A the output type of the decoding
  */
trait RowDecoder[+A]:

  /**
    * Decode the given input.
    * 
    * @param input the row (represented as a [[List]] of column values) to decode
    * @return either the decoded [[A]] or a [[CSVError]] if it failed
    */
  def decode(input: List[String]): Either[CSVError, A]

  /**
   * Map this decoder's output.
   *
   * @tparam B the new output type
   * @param f the function to apply to the output
   * @return a new decoder applying [[f]] to this decoder's input
   */
  def map[B](f: A => B): RowDecoder[B] = (input: List[String]) => decode(input).map(f)

  /**
    * Map this decoder's output or fail.
    *
    * @tparam B the new output type
    * @param f the function to apply to the output, which can either succeed or fail
    * @return a new decoder applying [[f]] to this decoder's input
    */
  def mapEither[B](f: A => Either[CSVError, B]): RowDecoder[B] = (input: List[String]) => decode(input).flatMap(f)

object RowDecoder:

  /**
    * Empty tuple decoder.
    */
  given RowDecoder[EmptyTuple] =
    case Nil => Right(EmptyTuple)
    case _ => Left(CSVError.InvalidColumnCount)

  /**
    * Non-empty tuple decoder.
    *
    * @tparam A the type of the head of the tuple
    * @param headDecoder the decoder of [[A]]
    * @param tailDecoder the decoder of [[T]] (the tail)
    */
  given [A, T <: Tuple](using headDecoder: ColumnDecoder[A], tailDecoder: RowDecoder[T]): RowDecoder[A *: T] =
    case head :: tail =>
      for
        decodedHead <- headDecoder.decode(head)
        decodedTail <- tailDecoder.decode(tail)
      yield
        decodedHead *: decodedTail

    case Nil => Left(CSVError.InvalidColumnCount)

  /**
    * Decode a value of type [[A]] from a tuple [[T]].
    *
    * @tparam A the final type to decode
    * @tparam T the type of fields
    * @param f the function map [[T]] to [[A]]
    * @param decoder the decoder of [[T]]
    * @return a decoder of [[A]] using [[decoder]] under the hood
    */
  def fromTuple[A, T <: Tuple](f: T => A)(using decoder: RowDecoder[T]): RowDecoder[A] =
    decoder.map(f)

  /**
    * Generate a [[RowDecoder]] for a product type.
    *
    * @param mirror the product metadata of [[T]]
    * @param fieldDecoder the decoder of the product's fields
    * @return a row decoder of [[T]]
    */
  inline def derived[T](using mirror: Mirror.ProductOf[T], fieldDecoder: RowDecoder[mirror.MirroredElemTypes]): RowDecoder[T] =
    fromTuple[T, mirror.MirroredElemTypes](mirror.fromTuple)
