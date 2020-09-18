package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.AbstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.model.*;

import java.time.LocalDate;
import java.util.*;

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
        return gliderFlightRepository.findByPicOrCopilotOrPicNameOrCopilotName(pilot, pilot, name, name);
    }


    public List<GliderFlight> getFilteredGliderFlightsByPilot(
            Pilot pilot,
            Boolean active,
            String from,
            String to,
            String task,
            Boolean pic,
            Boolean instructor,
            Aircraft aircraft,
            String type,
            String registration,
            String start
    ) {
        StringBuilder whereSectionBuilder = new StringBuilder();
        Map<String, Object> filters = new HashMap<>();
        AbstractFlightService.getWhereSectionFilteringFlightsByPilot(pilot, active, from, to, task, pic, instructor, aircraft, type, registration, whereSectionBuilder, filters);
        if (start != null && !start.isEmpty()) {
            whereSectionBuilder.append("f.startMethod = :start");
            filters.put("start",start);
        }
        String whereSection = whereSectionBuilder.toString();
        log.debug("\nWHERE SECTION \"{}\"", whereSection);
        return gliderFlightRepository.getFilteredGliderFlights(whereSection, filters);
    }
}
