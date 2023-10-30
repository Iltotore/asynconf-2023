package io.github.iltotore.asynconf

import cats.effect.IO

import io.github.iltotore.asynconf.csv.{readCSV, RowDecoder}

import scala.scalajs.js.Any.{*, given}
import scala.scalajs.js
import org.scalajs.dom.*

import js.Dynamic.global
import js.Dynamic

private val fetch = global.fetch

def readTextFile(file: String): IO[String] =
  IO
    .fromPromise(IO(fetch(file).`then`((x: Dynamic) => x.text()).asInstanceOf[js.Promise[String]]))

def loadCSV[A : RowDecoder](file: String): IO[List[A]] =
  readTextFile(file)
    .flatMap(text => IO.fromEither(readCSV[A](text)))