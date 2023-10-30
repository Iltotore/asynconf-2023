package io.github.iltotore.asynconf.model

case class InterestRegistry(
    vehicleInterests: Map[ValueRange[EcoScore], Percent],
    bonusInterests: Map[PassengerCount, Percent]
):

  def getVehicleInterest(score: EcoScore): Option[Percent] =
    vehicleInterests.collectFirst:
      case (range, interest) if range.includes(score) => interest

  def getBonusInterest(count: PassengerCount): Option[Percent] = bonusInterests.get(count)

object InterestRegistry:

  val empty: InterestRegistry = InterestRegistry(Map.empty, Map.empty)