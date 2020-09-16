package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngineFlightService {
    private final EngineFlightRepository engineFlightRepository;

    private Set<LocalDate> getAllFlyingDays(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate finish = LocalDate.of(year, 12, 31);
        return engineFlightRepository.getFlyingEngineDays(start, finish);
    }

    public Set<EngineFlight> getByDate(LocalDate date) {
        return engineFlightRepository.getDistinctByDateOrderByStart(date);
    }

    public EngineFlight save(EngineFlight flight) {
        if (flight.getInstructor()) {
            Pilot pic = flight.getCopilot();
            Pilot copilot = flight.getPic();
            flight.setPic(pic);
            flight.setCopilot(copilot);
        }
        return engineFlightRepository.save(flight);
    }

    public EngineFlight getById(Long id) {
        return engineFlightRepository.findFirstById(id);
    }

    public void update(EngineFlight engineFlight) {
        log.debug("\nCHECKING ENGINE FLIGHT {}", engineFlight);
        if (engineFlight != null && engineFlight.getId() != null) {
            log.debug("\nGETTING ENGINE-FLIGHT FROM DATABASE");
            EngineFlight toEdit = engineFlightRepository.findFirstById(engineFlight.getId());
            log.debug("\n SETTING ALL FIELDS IN ENGINE-FLIGHT TO EDIT \n OLD DATA {} \n NEW DATA{}", toEdit, engineFlight);
            toEdit.setActive(engineFlight.getActive());
            toEdit.setAircraftRegistrationNumber(engineFlight.getAircraftRegistrationNumber());
            toEdit.setCopilotName(engineFlight.getCopilotName());
            toEdit.setDate(engineFlight.getDate());
            toEdit.setFlightTime(engineFlight.getFlightTime());
            toEdit.setInstructor(engineFlight.getInstructor());
            toEdit.setPicName(engineFlight.getPicName());
            toEdit.setStart(engineFlight.getStart());
            toEdit.setTask(engineFlight.getTask());
            toEdit.setTouchdown(engineFlight.getTouchdown());
            toEdit.setCrew(engineFlight.getCrew());
            toEdit.setTow(engineFlight.getTow());
            toEdit.setAircraft(engineFlight.getAircraft());
            toEdit.setCopilot(engineFlight.getCopilot());
            toEdit.setPic(engineFlight.getPic());
            log.debug("SAVING ENGINE-FLIGHT WITH NEW DATA");
            engineFlightRepository.save(toEdit);
        }
    }

    private boolean isEveryFlightActive(LocalDate date) {
        boolean anyInactive = true;
        Set<EngineFlight> flights = getByDate(date);
        for (EngineFlight flight : flights) {
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
}
