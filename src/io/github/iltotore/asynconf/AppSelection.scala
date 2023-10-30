package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.InterestAttribute
import io.github.iltotore.asynconf.model.*

case class AppSelection(
    vehicleType: Option[VehicleName],
    energy: Option[EnergyName],
    mileage: Option[ValueRange[Kilometer]],
    year: Option[ValueRange[Year]],
    passengerCount: Option[PassengerCount]
):

  def updateAttribute[A](attribute: InterestAttribute[A], value: Option[A]): AppSelection = attribute match
    case InterestAttribute.VehicleType => this.copy(vehicleType = value)
    case InterestAttribute.Energy => this.copy(energy = value)
    case InterestAttribute.Mileage => this.copy(mileage = value)
    case InterestAttribute.Year => this.copy(year = value)
    case InterestAttribute.Passengers => this.copy(passengerCount = value)

object AppSelection:

  val empty: AppSelection = AppSelection(None, None, None, None, None)