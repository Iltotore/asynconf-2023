package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.*
import tyrian.Html
import tyrian.Html.*

def noneOption[A](attribute: InterestAttribute[A]): Html[AppMessage] =
  option(value := "none")("---")

def renderVehicleTypeOption(name: VehicleName, vehicleType: VehicleType): Html[AppMessage] =
  option(value := name.value)(
    label(name.value),
    label(s"Poids: ${vehicleType.weight}")
  )

def renderEnergyOption(name: EnergyName): Html[AppMessage] =
  option(value := name.value)(name.value)

def renderMileageOption(range: ValueRange[Kilometer]): Html[AppMessage] =
  option(value := range.toString)(s"$range km")

def renderYearOption(range: ValueRange[Year]): Html[AppMessage] =
  option(value := range.toString)(range.toString)

def renderPassengerOption(count: PassengerCount): Html[AppMessage] =
  option(value := count.toString)(count.toString)

def renderOptions[A, B](
    attribute: InterestAttribute[A],
    elements: Iterable[B],
    id: String,
    labelText: String,
    renderer: B => Html[AppMessage],
    reader: String => Option[A]
): Html[AppMessage] = div(
  label(`for` := id)(labelText),
  select(`name` := id, onChange(selected => AppMessage.Select(attribute, reader(selected))))(
    noneOption(attribute) :: elements.map(renderer).toList
  )
)

def renderVehicleTypes(vehicleTypes: Map[VehicleName, VehicleType]): Html[AppMessage] =
  renderOptions(InterestAttribute.VehicleType, vehicleTypes, "vehicle_type", "Type de véhicule:", renderVehicleTypeOption, VehicleName.option)

def renderEnergies(energies: Iterable[EnergyName]): Html[AppMessage] =
  renderOptions(InterestAttribute.Energy, energies, "energy", "Énergie:", renderEnergyOption, EnergyName.option)

def renderMileages(ranges: Iterable[ValueRange[Kilometer]]): Html[AppMessage] =
  def readMileage(value: String) = value.toIntOption.flatMap(Kilometer.option)
  renderOptions(InterestAttribute.Mileage, ranges, "mileage", "Kilométrage:", renderMileageOption, ValueRange.fromString(_, readMileage))

def renderYears(ranges: Iterable[ValueRange[Year]]): Html[AppMessage] =
  def readYear(value: String) = value.toIntOption.flatMap(Year.option)
  renderOptions(InterestAttribute.Year, ranges, "year", "Année:", renderYearOption, ValueRange.fromString(_, readYear))

def renderPassengers(passengers: Iterable[PassengerCount]): Html[AppMessage] =
  def readPassengerCount(value: String) = value.toIntOption.flatMap(PassengerCount.option)
  renderOptions(InterestAttribute.Passengers, passengers, "passenger_count", "Nombre de passagers", renderPassengerOption, readPassengerCount)
