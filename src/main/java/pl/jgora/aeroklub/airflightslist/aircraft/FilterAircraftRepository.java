package pl.jgora.aeroklub.airflightslist.aircraft;

import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.List;

public interface FilterAircraftRepository {
    List<Aircraft> filteringAircrafts(AircraftFilter aircraftFilter);
}
