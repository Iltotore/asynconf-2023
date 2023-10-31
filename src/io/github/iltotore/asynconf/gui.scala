package io.github.iltotore.asynconf

import io.github.iltotore.asynconf.model.*
import tyrian.Html
import tyrian.Html.*

/**
  * An "empty" option.
  */
val noneOption: Html[AppMessage] =
  option(value := "none")("---")

/**
  * Render a vehicle type option.
  *
  * @param name the name of the vehicle type
  * @param vehicleType extra infos about the vehicle type
  * @return an HTML option for this vehicle type
  */
def renderVehicleTypeOption(name: VehicleName, vehicleType: VehicleType): Html[AppMessage] =
  option(value := name.value)(
    label(name.value),
    label(s"Poids: ${vehicleType.weight}")
  )

/**
  * Render an energy type option.
  *
  * @param name the name of the energy type
  * @return an HTML option for this energy type
  */
def renderEnergyOption(name: EnergyName): Html[AppMessage] =
  option(value := name.value)(name.value)

/**
  * Render a mileage range option.
  *
  * @param range the mileage range
  * @return an HTML option for this range
  */
def renderMileageOption(range: ValueRange[Kilometer]): Html[AppMessage] =
  option(value := range.toString)(s"$range km")

/**
  * Render an age range option.
  *
  * @param range the age range
  * @return an HTML option for this range
  */
def renderYearOption(range: ValueRange[Year]): Html[AppMessage] =
  option(value := range.toString)(range.toString)

/**
  * Render a passenger count.
  *
  * @param count the passenger count
  * @return an HTML option for this count
  */
def renderPassengerOption(count: PassengerCount): Html[AppMessage] =
  option(value := count.toString)(count.toString)

/**
  * Render a `select` from the given options.
  *
  * @param attribute the attribute to render
  * @param elements the choices to display
  * @param id the id of the `select`
  * @param labelText the label of the select
  * @param renderer the renderer of an option
  * @param reader the reader of an option's value attribute
  * @return an HTML select representing the given attribute and options
  */
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
    noneOption :: elements.map(renderer).toList
  )
)

/**
  * Render the vehicle type choices
  *
  * @param vehicleTypes the available choices
  * @return an HTML select representing the vehicle types
  */
def renderVehicleTypes(vehicleTypes: Map[VehicleName, VehicleType]): Html[AppMessage] =
  renderOptions(InterestAttribute.VehicleType, vehicleTypes, "vehicle_type", "Type de véhicule:", renderVehicleTypeOption, VehicleName.option)

/**
  * Render the energy type choices
  *
  * @param energues the available choices
  * @return an HTML select representing the energy types
  */
def renderEnergies(energies: Iterable[EnergyName]): Html[AppMessage] =
  renderOptions(InterestAttribute.Energy, energies, "energy", "Énergie:", renderEnergyOption, EnergyName.option)

/**
  * Render the vehicle mileage choices
  *
  * @param ranges the available choices
  * @return an HTML select representing the milage ranges
  */
def renderMileages(ranges: Iterable[ValueRange[Kilometer]]): Html[AppMessage] =
  def readMileage(value: String) = value.toIntOption.flatMap(Kilometer.option)
  renderOptions(InterestAttribute.Mileage, ranges, "mileage", "Kilométrage:", renderMileageOption, ValueRange.fromString(_, readMileage))

/**
  * Render the vehicle age choices
  *
  * @param ranges the available choices
  * @return an HTML select representing the age ranges
  */
def renderYears(ranges: Iterable[ValueRange[Year]]): Html[AppMessage] =
  def readYear(value: String) = value.toIntOption.flatMap(Year.option)
  renderOptions(InterestAttribute.Year, ranges, "year", "Année:", renderYearOption, ValueRange.fromString(_, readYear))

/**
  * Render the passenger count choices
  *
  * @param passengers the available choices
  * @return an HTML select representing the vehicle passenger counts
  */
def renderPassengers(passengers: Iterable[PassengerCount]): Html[AppMessage] =
  def readPassengerCount(value: String) = value.toIntOption.flatMap(PassengerCount.option)
  renderOptions(InterestAttribute.Passengers, passengers, "passenger_count", "Nombre de passagers", renderPassengerOption, readPassengerCount)
