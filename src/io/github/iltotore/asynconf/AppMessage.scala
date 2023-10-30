package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.*

enum AppMessage:
  case None
  case Select[A](attribute: InterestAttribute[A], value: Option[A])
  case LoadedScoreInterest(interest: Map[ValueRange[EcoScore], Percent])
  case LoadedBonusInterest(interest: Map[PassengerCount, Percent])
  case LoadedVehicleTypes(interest: Map[VehicleName, VehicleType])
  case LoadedEnergyScores(interest: Map[EnergyName, EcoScore])
  case LoadedMileageScores(interest: Map[ValueRange[Kilometer], EcoScore])
  case LoadedYearScores(interest: Map[ValueRange[Year], EcoScore])