package io.github.iltotore.asynconf.model

/**
  * The registry of interest tables, used to estimate the interest rate.
  *
  * @param vehicleInterests the interest rates by eco score
  * @param bonusInterests the bonus interest rates by passenger count
  */
case class InterestRegistry(
    vehicleInterests: Map[ValueRange[EcoScore], Percent],
    bonusInterests: Map[PassengerCount, Percent]
):

  /**
    * Get the interest rate of the given score.
    *
    * @param score the score used to estimate interest rate
    * @return the estimated interest rate
    */
  def getVehicleInterest(score: EcoScore): Option[Percent] =
    vehicleInterests.collectFirst:
      case (range, interest) if range.includes(score) => interest

  /**
    * Get the interest bonus of the given passenger count.
    *
    * @param count the number of usual passenger in the vehicle
    * @return the estimated interest bonus
    */
  def getBonusInterest(count: PassengerCount): Option[Percent] = bonusInterests.get(count)

object InterestRegistry:

  /**
    * An empty [[InterestRegistry]].
    */
  val empty: InterestRegistry = InterestRegistry(Map.empty, Map.empty)