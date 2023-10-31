package io.github.iltotore.asynconf.model

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/**
 * A vehicle type name.
 */
opaque type VehicleName = String :| Not[Blank]
object VehicleName extends RefinedTypeOps[String, Not[Blank], VehicleName]

/**
 * An energy name.
 */
opaque type EnergyName = String :| Not[Blank]
object EnergyName extends RefinedTypeOps[String, Not[Blank], EnergyName]

/**
 * A weight in kilograms.
 */
opaque type Kilogram = Int :| Positive
object Kilogram extends RefinedTypeOps[Int, Positive, Kilogram]:

  given Numeric[Kilogram] = Numeric.IntIsIntegral.asInstanceOf

/**
 * A distance in kilometers.
 */
opaque type Kilometer = Int :| Positive
object Kilometer extends RefinedTypeOps[Int, Positive, Kilometer]:

  given Numeric[Kilometer] = Numeric.IntIsIntegral.asInstanceOf

/**
 * The score of a vehicle.
 */
opaque type EcoScore = Double :| Interval.Closed[0, 40]
object EcoScore extends RefinedTypeOps[Double, Interval.Closed[0, 40], EcoScore]:

  given Numeric[EcoScore] = Numeric.IntIsIntegral.asInstanceOf

/**
 * A percentage.
 */
opaque type Percent = Double :| Pure
object Percent extends RefinedTypeOps[Double, Pure, Percent]:

  given Numeric[Percent] = Numeric.DoubleIsFractional.asInstanceOf

/**
 * A year, more recent than 1960.
 */
opaque type Year = Int :| GreaterEqual[1960]
object Year extends RefinedTypeOps[Int, GreaterEqual[1960], Year]:

  given Numeric[Year] = Numeric.IntIsIntegral.asInstanceOf

/**
 * The (positive) passenger count.
 */
opaque type PassengerCount = Int :| GreaterEqual[0]
object PassengerCount extends RefinedTypeOps[Int, GreaterEqual[0], PassengerCount]:

  given Numeric[PassengerCount] = Numeric.IntIsIntegral.asInstanceOf

extension [K, V](map: Map[K, V])

  /**
    * Get the value associated with [[key]] or return an [[InterestError]].
    *
    * @param key the key associated to the value to get
    * @param attribute the attribute related to the value to get (used to build the error)
    * @return the value associated with the given key or an [[InterestError]]
    */
  def getOrIllegalValue(key: K, attribute: InterestAttribute[K]): Either[InterestError.IllegalValue[K], V] =
    map.get(key).toRight(InterestError.IllegalValue(attribute, key))