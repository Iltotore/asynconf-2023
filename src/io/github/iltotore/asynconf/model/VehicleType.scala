package io.github.iltotore.asynconf.model

import io.github.iltotore.iron.*
import io.github.iltotore.asynconf.csv.{ColumnDecoder, RowDecoder}

case class VehicleType(weight: ValueRange[Kilogram], score: EcoScore) derives RowDecoder