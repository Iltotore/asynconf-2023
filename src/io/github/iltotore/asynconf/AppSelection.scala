package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.InterestAttribute
import io.github.iltotore.asynconf.model.*

/**
  * The selection of the app user.
  *
  * @param vehicleType the selected vehicle type
  * @param energy the selected energy
  * @param mileage the selected mileage range
  * @param year the selected age range
  * @param passengerCount the selected passenger count
  */
case class AppSelection(
    vehicleType: Option[VehicleName],
    energy: Option[EnergyName],
    mileage: Option[ValueRange[Kilometer]],
    year: Option[ValueRange[Year]],
    passengerCount: Option[PassengerCount]
):

  /**
    * Update an attribute of this selection.
    *
    * @param attribute the updated attribute
    * @param value the new value
    * @return a new [[AppSelection]] with the given attribute modified
    */
  def updateAttribute[A](attribute: InterestAttribute[A], value: Option[A]): AppSelection = attribute match
    case InterestAttribute.VehicleType => this.copy(vehicleType = value)
    case InterestAttribute.Energy => this.copy(energy = value)
    case InterestAttribute.Mileage => this.copy(mileage = value)
    case InterestAttribute.Year => this.copy(year = value)
    case InterestAttribute.Passengers => this.copy(passengerCount = value)

object AppSelection:

  /**
    * An empty selection.
    */
  val empty: AppSelection = AppSelection(None, None, None, None, None)