package pl.jgora.aeroklub.airflightslist.AbstractFlight;

import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import java.util.List;
import java.util.Map;

public interface FilterFlightsRepository {
    List<EngineFlight> getFilteredEngineFlights(String whereSection, Map<String, Object> filters);

    List<GliderFlight> getFilteredGliderFlights(String whereSection, Map<String, Object> filters);
}

