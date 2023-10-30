package io.github.iltotore.asynconf.model

import scala.math.Numeric.Implicits.infixNumericOps

case class ScoreRegistry(
    vehicleTypes: Map[VehicleName, VehicleType],
    energyTypes: Map[EnergyName, EcoScore],
    mileages: Map[ValueRange[Kilometer], EcoScore],
    years: Map[ValueRange[Year], EcoScore]
):

  def getScore(
      vehicleType: VehicleName,
      energy: EnergyName,
      mileage: ValueRange[Kilometer],
      year: ValueRange[Year]
  ): Either[InterestError, EcoScore] =
    for
      vehicleScore <- vehicleTypes.getOrIllegalValue(vehicleType, InterestAttribute.VehicleType).map(_.score)
      energyScore  <- energyTypes.getOrIllegalValue(energy, InterestAttribute.Energy)
      mileageScore <- mileages.getOrIllegalValue(mileage, InterestAttribute.Mileage)
      yearScore    <- years.getOrIllegalValue(year, InterestAttribute.Year)
    yield
      vehicleScore + energyScore + mileageScore + yearScore

object ScoreRegistry:

  val empty: ScoreRegistry = ScoreRegistry(Map.empty, Map.empty, Map.empty, Map.empty)