package io.github.iltotore.asynconf.model

import scala.math.Numeric.Implicits.infixNumericOps

/**
  * The registry of score tables. Used to calculate the score of a profile.
  *
  * @param vehicleTypes the scores by vehicle type
  * @param energyTypes the scores by energy type
  * @param mileages the scores by mileage (in kilometers)
  * @param years the scores by age (in year)
  */
case class ScoreRegistry(
    vehicleTypes: Map[VehicleName, VehicleType],
    energyTypes: Map[EnergyName, EcoScore],
    mileages: Map[ValueRange[Kilometer], EcoScore],
    years: Map[ValueRange[Year], EcoScore]
):

  /**
    * Estimate the score of a profile.
    *
    * @param vehicleType the type of vehicle
    * @param energy the type of used energy
    * @param mileage the mileage of the vehicle
    * @param year the age of the vehicle
    * @return the estimated score of the given vehicle profile
    */
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

  /**
   * An empty registry.
   */
  val empty: ScoreRegistry = ScoreRegistry(Map.empty, Map.empty, Map.empty, Map.empty)