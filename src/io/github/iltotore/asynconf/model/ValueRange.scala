package io.github.iltotore.asynconf.model

import io.github.iltotore.asynconf.csv.ColumnDecoder

import scala.math.Ordering.Implicits.infixOrderingOps

enum ValueRange[A : Ordering]:
  case Less(max: A)(using Ordering[A])
  case Greater(min: A)(using Ordering[A])
  case Between(min: A, max: A)(using Ordering[A])

  def includes(value: A): Boolean = this match
    case Less(max) => value <= max
    case Greater(min) => value >= min
    case Between(min, max) => value >= min && value <= max

  override def toString: String = this match
    case Less(max) => s"*-$max"
    case Greater(min) => s"$min-*"
    case Between(min, max) => s"$min-$max"

object ValueRange:

  def fromString[A : Ordering](value: String, elementReader: String => Option[A]): Option[ValueRange[A]] = value match
    case s"*-$left" => elementReader(left).map(Less.apply)
    case s"$right-*" => elementReader(right).map(Greater.apply)
    case s"$left-$right" =>
      for
        min <- elementReader(left)
        max <- elementReader(right)
      yield
        Between(min, max)

  given decoder[A : Ordering](using decoder: ColumnDecoder[A]): ColumnDecoder[ValueRange[A]] = ColumnDecoder.string.mapEither:
    case s"*-$left" => decoder.decode(left).map(Less.apply)
    case s"$right-*" => decoder.decode(right).map(Greater.apply)
    case s"$left-$right" =>
      for
        min <- decoder.decode(left)
        max <- decoder.decode(right)
      yield
        Between(min, max)
