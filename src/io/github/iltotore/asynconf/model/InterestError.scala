package io.github.iltotore.asynconf.model

enum InterestError:
  case IllegalValue[A](attribute: InterestAttribute[A], value: A)
  case UnexpectedScore(score: EcoScore)
  case Miscellaneous(msg: String)

  def message: String = this match
    case IllegalValue(attribute, value) => s"Value $value is invalid for attribute $attribute"
    case UnexpectedScore(score) => s"Unexpected score: $score"
    case Miscellaneous(msg) => msg