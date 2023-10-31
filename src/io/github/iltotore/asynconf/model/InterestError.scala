package io.github.iltotore.asynconf.model

/**
  * An error related to interest estimation.
  */
enum InterestError:

  /**
    * A value cannot be processed.
    *
    * @param attribute the value's attribute
    * @param value the value that cannot be processed
    */
  case IllegalValue[A](attribute: InterestAttribute[A], value: A)

  /**
    * The score cannot be processed
    *
    * @param score the invalid score
    */
  case UnexpectedScore(score: EcoScore)

  /**
    * A miscellneous error
    *
    * @param msg the error's message
    */
  case Miscellaneous(msg: String)

  /**
    * The error's message
    */
  def message: String = this match
    case IllegalValue(attribute, value) => s"Value $value is invalid for attribute $attribute"
    case UnexpectedScore(score) => s"Unexpected score: $score"
    case Miscellaneous(msg) => msg