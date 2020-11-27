package pl.jgora.aeroklub.airflightslist.aircraft;

import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.List;
import java.util.Map;

public interface FilterAircraftRepository {
    List<Aircraft> filteringAircrafts(AircraftFilter aircraftFilter);
}
