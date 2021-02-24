package pl.jgora.aeroklub.airflightslist.model;

import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AircraftSummary {
    private String aircraftType;
    private String aircraftRegistrationNumber;
    private Integer numberOfFlights;
    private Integer totalTime;

    public AircraftSummary(String aircraftsRegistration, Set<AbstractFlight> flightList) {
        this.aircraftType = flightList
                .stream()
                .filter(flight -> aircraftsRegistration.equals(flight.getAircraftRegistrationNumber()))
                .map(AbstractFlight::getAircraftType)
                .findFirst()
                .orElse("UNKNOWN TYPE");
        this.aircraftRegistrationNumber = aircraftsRegistration;
        List<AbstractFlight> thisAircraftFlights = flightList.stream().filter(flight -> aircraftsRegistration.equals(flight.getAircraftRegistrationNumber()))
                .collect(Collectors.toList());
        this.numberOfFlights = thisAircraftFlights.size();
        this.totalTime = thisAircraftFlights.stream().map(AbstractFlight::getFlightTime).reduce(0, Integer::sum);
    }
}
