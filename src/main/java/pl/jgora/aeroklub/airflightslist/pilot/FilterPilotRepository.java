package pl.jgora.aeroklub.airflightslist.pilot;

import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;

public interface FilterPilotRepository {
    List<Pilot> getFilteredPilots(PilotFilter pilotFilter);
}
