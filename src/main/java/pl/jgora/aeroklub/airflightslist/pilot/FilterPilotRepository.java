package pl.jgora.aeroklub.airflightslist.pilot;

import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;
import java.util.Map;

public interface FilterPilotRepository {
    List<Pilot> filteringPilots(String whereSection, Map<String,String> filters);
}
