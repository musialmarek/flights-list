package pl.jgora.aeroklub.airflightslist.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
public class ListSummary {
    private Integer totalFlights;
    private Integer totalTime;
    private List<AircraftSummary> aircraftSummary= new ArrayList<>();

    public ListSummary(Set<AbstractFlight> flights) {
        this.totalFlights = flights.size();
        this.totalTime = flights
                .stream()
                .map(AbstractFlight::getFlightTime)
                .reduce(0,Integer::sum);
        Set<String> aircraftsRegistrations = flights
                .stream()
                .map(AbstractFlight::getAircraftRegistrationNumber)
                .collect(Collectors.toSet());
        for (String aircraftsRegistration : aircraftsRegistrations) {
            aircraftSummary.add(new AircraftSummary(aircraftsRegistration,flights));
        }
    }
}
