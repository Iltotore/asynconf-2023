package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.*

/**
  * An application message/event.
  */
enum AppMessage:

  /**
    * Nothing happened.
    */
  case None

  /**
    * The user selected a new value in the calculator.
    *
    * @param attribute the changed attribute
    * @param value the new value
    */
  case Select[A](attribute: InterestAttribute[A], value: Option[A])

  /**
    * The interest rate by score has been loaded.
    */
  case LoadedScoreInterest(interest: Map[ValueRange[EcoScore], Percent])

  /**
    * The interest rate by passenger count has been loaded.
    */
  case LoadedBonusInterest(interest: Map[PassengerCount, Percent])

  /**
    * The vehicle types have been loaded.
    */
  case LoadedVehicleTypes(types: Map[VehicleName, VehicleType])

  /**
    * The score by energy type has been loaded.
    */
  case LoadedEnergyScores(scores: Map[EnergyName, EcoScore])

  /**
    * The score by mileage has been loaded.
    */
  case LoadedMileageScores(scores: Map[ValueRange[Kilometer], EcoScore])

  /**
    * The score by age has been loaded.
    */
  case LoadedYearScores(scores: Map[ValueRange[Year], EcoScore])