package io.github.iltotore.asynconf.model

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

opaque type VehicleName = String :| Not[Blank]
object VehicleName extends RefinedTypeOps[String, Not[Blank], VehicleName]

opaque type EnergyName = String :| Not[Blank]
object EnergyName extends RefinedTypeOps[String, Not[Blank], EnergyName]

opaque type Kilogram = Int :| Positive
object Kilogram extends RefinedTypeOps[Int, Positive, Kilogram]:

  given Numeric[Kilogram] = Numeric.IntIsIntegral.asInstanceOf

opaque type Kilometer = Int :| Positive
object Kilometer extends RefinedTypeOps[Int, Positive, Kilometer]:

  given Numeric[Kilometer] = Numeric.IntIsIntegral.asInstanceOf

opaque type EcoScore = Double :| Interval.Closed[0, 40]
object EcoScore extends RefinedTypeOps[Double, Interval.Closed[0, 40], EcoScore]:

  given Numeric[EcoScore] = Numeric.IntIsIntegral.asInstanceOf

opaque type Percent = Double :| Pure
object Percent extends RefinedTypeOps[Double, Pure, Percent]:

  given Numeric[Percent] = Numeric.DoubleIsFractional.asInstanceOf

opaque type Year = Int :| GreaterEqual[1960]
object Year extends RefinedTypeOps[Int, GreaterEqual[1960], Year]:

  given Numeric[Year] = Numeric.IntIsIntegral.asInstanceOf

opaque type PassengerCount = Int :| GreaterEqual[0]
object PassengerCount extends RefinedTypeOps[Int, GreaterEqual[0], PassengerCount]:

  given Numeric[PassengerCount] = Numeric.IntIsIntegral.asInstanceOf

extension [K, V](map: Map[K, V])

  def getOrIllegalValue(key: K, attribute: InterestAttribute[K]): Either[InterestError.IllegalValue[K], V] =
    map.get(key).toRight(InterestError.IllegalValue(attribute, key))