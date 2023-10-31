package io.github.iltotore.asynconf

import scala.math.Numeric.Implicits.infixNumericOps
import io.github.iltotore.asynconf.model.{EcoScore, InterestRegistry, Percent, ScoreRegistry}

/**
  * The state of the web app.
  *
  * @param scoreRegistry the loaded score registry
  * @param interestRegistry the loaded interest registry
  * @param selection the user's current selection
  */
case class App(scoreRegistry: ScoreRegistry, interestRegistry: InterestRegistry, selection: AppSelection):

  /**
    * The estimated score of the current selection, if complete.
    */
  def score: Option[EcoScore] =
    for
      vehicleType <- selection.vehicleType
      energy <- selection.energy
      mileage <- selection.mileage
      year <- selection.year
      result <- scoreRegistry.getScore(vehicleType, energy, mileage, year).toOption
    yield
      result

  /**
    * The estimated interest rate of the current selection if complete.
    */
  def interest: Option[Percent] =
    for
      sc <- score
      passengerCount <- selection.passengerCount
      vehicleInterest <- interestRegistry.getVehicleInterest(sc)
      bonusInterest <- interestRegistry.getBonusInterest(passengerCount)
    yield
      vehicleInterest + bonusInterest

object App:

  /**
    * An empty (default) app state.
    */
  val empty: App = App(ScoreRegistry.empty, InterestRegistry.empty, AppSelection.empty)
