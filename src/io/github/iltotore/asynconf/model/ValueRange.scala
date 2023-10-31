package io.github.iltotore.asynconf.model

import io.github.iltotore.asynconf.csv.ColumnDecoder

import scala.math.Ordering.Implicits.infixOrderingOps

/**
  * An interval of values of type [[A]].
  * 
  * @tparam A the values' type
  */
enum ValueRange[A : Ordering]:

  /**
    * A range from -inf to [[max]].
    *
    * @param max the upper bound
    */
  case Less(max: A)(using Ordering[A])

  /**
    * A range from [[min]] to +inf.
    *
    * @param min the lower bound
    */
  case Greater(min: A)(using Ordering[A])

  /**
    * A range between [[min]] and [[max]].
    *
    * @param min the lower bound
    * @param max the upper bound
    */
  case Between(min: A, max: A)(using Ordering[A])

  /**
    * Check if this range contains the given value.
    *
    * @param value the value to check
    * @return `true` if the given value is included in this range
    */
  def includes(value: A): Boolean = this match
    case Less(max) => value <= max
    case Greater(min) => value >= min
    case Between(min, max) => value >= min && value <= max

  override def toString: String = this match
    case Less(max) => s"*-$max"
    case Greater(min) => s"$min-*"
    case Between(min, max) => s"$min-$max"

object ValueRange:

  /**
    * Read a [[ValueRange]] from the given to [[String]].
    *
    * @tparam A the contained values' type
    * @param value the text to parse
    * @param elementReader the reader of an element of type [[A]]
    * @return the parsed [[ValueRange]] or [[None]] if the decoding failed
    */
  def fromString[A : Ordering](value: String, elementReader: String => Option[A]): Option[ValueRange[A]] = value match
    case s"*-$left" => elementReader(left).map(Less.apply)
    case s"$right-*" => elementReader(right).map(Greater.apply)
    case s"$left-$right" =>
      for
        min <- elementReader(left)
        max <- elementReader(right)
      yield
        Between(min, max)
    case _ => None

  given decoder[A : Ordering](using decoder: ColumnDecoder[A]): ColumnDecoder[ValueRange[A]] = ColumnDecoder.string.mapEither:
    case s"*-$left" => decoder.decode(left).map(Less.apply)
    case s"$right-*" => decoder.decode(right).map(Greater.apply)
    case s"$left-$right" =>
      for
        min <- decoder.decode(left)
        max <- decoder.decode(right)
      yield
        Between(min, max)
