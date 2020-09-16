package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.AbstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import java.time.LocalDate;
import java.util.LinkedHashMap;
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
        log.debug("\n SAVING TOW FLIGHT {}", engineFlight);
        engineFlightService.save(engineFlight);
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

}
