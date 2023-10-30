package io.github.iltotore.asynconf.csv

enum CSVError(message: String) extends Throwable(message):
  case InvalidValue(value: String, msg: String) extends CSVError(s"Invalid value $value: $msg")
  case InvalidColumnCount extends CSVError("Invalid column count.")
