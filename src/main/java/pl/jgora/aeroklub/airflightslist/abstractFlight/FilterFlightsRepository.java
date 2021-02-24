package pl.jgora.aeroklub.airflightslist.abstractFlight;

import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Note;

import java.util.List;

public interface FilterFlightsRepository {
    List<EngineFlight> getFilteredEngineFlights(FlightsFilter flightsFilter);

    List<GliderFlight> getFilteredGliderFlights(FlightsFilter flightsFilter);

    List<Note> getFilteredNotes(FlightsFilter filter);
}

