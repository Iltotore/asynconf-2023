package io.github.iltotore.asynconf.model

enum InterestAttribute[A]:
  case VehicleType extends InterestAttribute[VehicleName]
  case Energy extends InterestAttribute[EnergyName]
  case Mileage extends InterestAttribute[ValueRange[Kilometer]]
  case Year extends InterestAttribute[ValueRange[Year]]
  case Passengers extends InterestAttribute[PassengerCount]