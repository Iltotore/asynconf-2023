package io.github.iltotore.asynconf.model

import io.github.iltotore.asynconf.csv.RowDecoder

case class Vehicle(name: VehicleName, weight: ValueRange[Kilogram], score: EcoScore) derives RowDecoder:

  def vehicleType: VehicleType = VehicleType(weight, score)
