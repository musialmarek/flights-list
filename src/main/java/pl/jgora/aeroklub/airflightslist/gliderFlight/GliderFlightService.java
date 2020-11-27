package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.model.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GliderFlightService {
    private final GliderFlightRepository gliderFlightRepository;
    private final EngineFlightService engineFlightService;

    public Set<LocalDate> getAllFlyingDays(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate finish = LocalDate.of(year, 12, 31);
        return gliderFlightRepository.getFlyingGliderDays(start, finish);
    }

    public Set<GliderFlight> getByDate(LocalDate date) {
        return gliderFlightRepository.getDistinctByDateOrderByStart(date);
    }

    public GliderFlight save(GliderFlight flight) {
        AbstractFlightService.replacePilots(flight);
        EngineFlight engineFlight = flight.getEngineFlight();
        if (engineFlight != null) {
            log.debug("\n SAVING TOW FLIGHT {}", engineFlight);
            engineFlightService.save(engineFlight);
        }
        log.debug("\n SAVING GLIDER FLIGHT {}", flight);
        return gliderFlightRepository.save(flight);
    }

    public GliderFlight getById(Long id) {
        return gliderFlightRepository.findFirstById(id);
    }

    public void update(GliderFlight flight) {
        log.debug("\nCHECKING GLIDER FLIGHT {}", flight);
        if (flight != null && flight.getId() != null) {
            log.debug("\nGETTING GLIDER-FLIGHT FROM DATABASE");
            GliderFlight toEdit = gliderFlightRepository.findFirstById(flight.getId());
            log.debug("\n SETTING ALL FIELDS IN GLIDER-FLIGHT TO EDIT \n OLD DATA {} \n NEW DATA{}", toEdit, flight);
            toEdit.setStartMethod(flight.getStartMethod());
            toEdit.setEngineFlight(null);
            if (flight.getStartMethod().equals(StartMethod.ATTO)) {
                toEdit.setEngineFlight(engineFlightService.update(flight.getEngineFlight()));
            }
            AbstractFlightService.updateFlight(flight, toEdit);
            log.debug("SAVING GLIDER-FLIGHT WITH NEW DATA");
            gliderFlightRepository.save(toEdit);
        }
    }

    private boolean isEveryFlightActive(LocalDate date) {
        boolean anyInactive = true;
        Set<GliderFlight> flights = getByDate(date);
        for (GliderFlight flight : flights) {
            if (!flight.getActive()) {
                anyInactive = false;
                break;
            }
        }
        return anyInactive;
    }

    Map<LocalDate, Boolean> getDatesAndActives(int year) {
        Map<LocalDate, Boolean> datesAndActives = new LinkedHashMap<>();
        Set<LocalDate> allFlyingDays = getAllFlyingDays(year);
        allFlyingDays.forEach(date -> datesAndActives.put(date, isEveryFlightActive(date)));
        return datesAndActives;
    }

    List<GliderFlight> getByPilot(Pilot pilot) {
        String name = pilot.getName();
        return gliderFlightRepository.findByPicOrCopilotOrPicNameOrCopilotNameOrderByDateAscStart(pilot, pilot, name, name);
    }


    public List<GliderFlight> getFilteredGliderFlightsByPilot(FlightsFilter flightsFilter) {
        return gliderFlightRepository.getFilteredGliderFlights(flightsFilter);
    }

    public List<GliderFlight> getFilteredFlightsByAircraft(FlightsFilter flightsFilter) {
        return gliderFlightRepository.getFilteredGliderFlights(flightsFilter);
    }

    public List<GliderFlight> getAllByAircraft(Aircraft aircraft) {
        String type = aircraft.getType();
        String registrationNumber = aircraft.getRegistrationNumber();
        return gliderFlightRepository.findByAircraftOrAircraftTypeAndAircraftRegistrationNumberOrderByDateAscStart(aircraft, type, registrationNumber);
    }
}
