package io.github.iltotore.asynconf.model

import io.github.iltotore.asynconf.csv.RowDecoder

/**
 * A vehicle score row.
 * 
 * @param name the vehicle's type name
 * @param weight the vehicle's weight range in kilograms
 * @param score the score associated with this archetype
 */
case class Vehicle(name: VehicleName, weight: ValueRange[Kilogram], score: EcoScore) derives RowDecoder:

  /**
   * The vehicle's type without its name.
   */
  def vehicleType: VehicleType = VehicleType(weight, score)
