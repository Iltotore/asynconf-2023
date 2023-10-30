package io.github.iltotore.asynconf

import scala.math.Numeric.Implicits.infixNumericOps
import io.github.iltotore.asynconf.model.{EcoScore, InterestRegistry, Percent, ScoreRegistry}

case class App(scoreRegistry: ScoreRegistry, interestRegistry: InterestRegistry, selection: AppSelection):

  def score: Option[EcoScore] =
    for
      vehicleType <- selection.vehicleType
      energy <- selection.energy
      mileage <- selection.mileage
      year <- selection.year
      result <- scoreRegistry.getScore(vehicleType, energy, mileage, year).toOption
    yield
      result

  def interest: Option[Percent] =
    for
      sc <- score
      passengerCount <- selection.passengerCount
      vehicleInterest <- interestRegistry.getVehicleInterest(sc)
      bonusInterest <- interestRegistry.getBonusInterest(passengerCount)
    yield
      vehicleInterest + bonusInterest

object App:

  val empty: App = App(ScoreRegistry.empty, InterestRegistry.empty, AppSelection.empty)
