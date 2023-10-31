package io.github.iltotore.asynconf.csv

/**
  * A CSV-related error.
  *
  * @param message the error message
  */
enum CSVError(message: String) extends Throwable(message):

  /**
    * Tried to decode an invalid value.
    * 
    * @param value the text that failed to be decoded
    * @param msg further information about the error
    */
  case InvalidValue(value: String, msg: String) extends CSVError(s"Invalid value $value: $msg")

  /**
    * Too much or not enough columns.
    */
  case InvalidColumnCount extends CSVError("Invalid column count.")
