package io.github.iltotore.asynconf

import cats.effect.IO

import scala.scalajs.js.annotation.*
import io.github.iltotore.asynconf.csv.readCSV
import io.github.iltotore.asynconf.model.*
import io.github.iltotore.asynconf.csv.{ColumnDecoder, RowDecoder}
import tyrian.*
import tyrian.Html.*

import scala.collection.immutable.ListMap

@JSExportTopLevel("TyrianApp")
object Main extends TyrianApp[AppMessage, App]:

  /**
   * The routes of the app (none).
   */
  def router: Location => AppMessage = Routing.none(AppMessage.None)

  /**
   * The initialization function.
   * 
   * @param flags the launch flags passed to the app
   * @return the initial state of the app and tasks to execute
   */
  def init(flags: Map[String, String]): (App, Cmd[IO, AppMessage]) =

    val loadScoreInterest = loadCSV[(ValueRange[EcoScore], Percent)]("./resources/interest/score.csv").map(_.to(ListMap))
    val loadPassengerInterest = loadCSV[(PassengerCount, Percent)]("./resources/interest/passenger.csv").map(_.to(ListMap))

    val loadVehicleScores = loadCSV[Vehicle]("./resources/score/vehicle.csv")
      .map(_.map(x => (x.name, x.vehicleType)))
      .map(_.toMap)
    val loadEnergyScores = loadCSV[(EnergyName, EcoScore)]("./resources/score/energy.csv").map(_.to(ListMap))
    val loadMileageScores = loadCSV[(ValueRange[Kilometer], EcoScore)]("./resources/score/mileage.csv").map(_.to(ListMap))
    val loadYearScores = loadCSV[(ValueRange[Year], EcoScore)]("./resources/score/year.csv").map(_.to(ListMap))

    val cmd = Cmd.combineAll(List(
      Cmd.Run(loadScoreInterest, AppMessage.LoadedScoreInterest.apply),
      Cmd.Run(loadPassengerInterest, AppMessage.LoadedBonusInterest.apply),
      Cmd.Run(loadVehicleScores, AppMessage.LoadedVehicleTypes.apply),
      Cmd.Run(loadEnergyScores, AppMessage.LoadedEnergyScores.apply),
      Cmd.Run(loadMileageScores, AppMessage.LoadedMileageScores.apply),
      Cmd.Run(loadYearScores, AppMessage.LoadedYearScores.apply),
    ))

    (App.empty, cmd)

  /**
   * Update the given app according to the passed events.
   * 
   * @param app the current state of the app
   * @return a function taking an event and returning the updated app state
   */
  def update(app: App): AppMessage => (App, Cmd[IO, AppMessage]) =

    case AppMessage.LoadedScoreInterest(interest) =>
      println(s"Loaded score interest")
      (app.copy(interestRegistry = app.interestRegistry.copy(vehicleInterests = interest)), Cmd.None)

    case AppMessage.LoadedBonusInterest(interest) =>
      println(s"Loaded bonus interest")
      (app.copy(interestRegistry = app.interestRegistry.copy(bonusInterests = interest)), Cmd.None)

    case AppMessage.LoadedVehicleTypes(scores) =>
      println(s"Loaded vehicle interest")
      (app.copy(scoreRegistry = app.scoreRegistry.copy(vehicleTypes = scores)), Cmd.None)

    case AppMessage.LoadedEnergyScores(scores) =>
      println(s"Loaded energy scores")
      (app.copy(scoreRegistry = app.scoreRegistry.copy(energyTypes = scores)), Cmd.None)

    case AppMessage.LoadedMileageScores(scores) =>
      println(s"Loaded mileage scores")
      (app.copy(scoreRegistry = app.scoreRegistry.copy(mileages = scores)), Cmd.None)

    case AppMessage.LoadedYearScores(scores) =>
      println(s"Loaded year scores")
      (app.copy(scoreRegistry = app.scoreRegistry.copy(years = scores)), Cmd.None)

    case AppMessage.Select(attribute, value) =>
      println(s"Select $attribute: $value")
      (app.copy(selection = app.selection.updateAttribute(attribute, value)), Cmd.None)

    case _ => (app, Cmd.None)

  /**
   * Render the given app state.
   * 
   * @param app the current app state
   * @return the HTML node representing the given state
   */
  def view(app: App): Html[AppMessage] =
    val scoreRegistry = app.scoreRegistry

    div(
      h1("Green Bank"),
      h2("Calculez votre taux d'intérêt"),
      renderVehicleTypes(scoreRegistry.vehicleTypes),
      renderEnergies(scoreRegistry.energyTypes.keys),
      renderMileages(scoreRegistry.mileages.keys),
      renderYears(scoreRegistry.years.keys),
      renderPassengers(app.interestRegistry.bonusInterests.keys),
      app.score.fold(div())(score => label(s"Score: $score")),
      app.interest.fold(div())(interest => label(s"Taux d'intérêt: $interest"))
    )

  /**
   * The app's subscriptions/background repating tasks.
   */
  def subscriptions(model: App): Sub[IO, AppMessage] =
    Sub.None
