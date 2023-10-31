package io.github.iltotore.asynconf.model

/**
  * An attribute used in interest estimation.
  * 
  * @tparam A the type of values bound to this attribute
  */
enum InterestAttribute[A]:

  /**
    * The vehicle's type.
    */
  case VehicleType extends InterestAttribute[VehicleName]

  /**
    * The vehicle's used energy.
    */
  case Energy extends InterestAttribute[EnergyName]

  /**
    * The vehicle's mileage in kilometers.
    */
  case Mileage extends InterestAttribute[ValueRange[Kilometer]]

  /**
    * The vehicle's year.
    */
  case Year extends InterestAttribute[ValueRange[Year]]

  /**
    * The owner's usual passenger count.
    */
  case Passengers extends InterestAttribute[PassengerCount]