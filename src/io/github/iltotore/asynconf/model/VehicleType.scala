package io.github.iltotore.asynconf.model

import io.github.iltotore.iron.*
import io.github.iltotore.asynconf.csv.{ColumnDecoder, RowDecoder}

/**
  * A vehicle type.
  *
  * @param weight the type's weight range
  * @param score the score associated with this type
  */
case class VehicleType(weight: ValueRange[Kilogram], score: EcoScore) derives RowDecoder