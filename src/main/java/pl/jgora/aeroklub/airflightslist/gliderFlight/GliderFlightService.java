package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
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
        if (flight.getInstructor()) {
            log.debug("\n SWAPPING PLACES - FLIGHT IS INSTRUCTOR FLIGHT ");
            Pilot pic = flight.getCopilot();
            Pilot copilot = flight.getPic();
            flight.setPic(pic);
            flight.setCopilot(copilot);
        }
        EngineFlight engineFlight = flight.getEngineFlight();
        log.debug("\n SAVING TOW FLIGHT {}", engineFlight);
        engineFlightService.save(engineFlight);
        log.debug("\n SAVING GLIDER FLIGHT {}", flight);
        return gliderFlightRepository.save(flight);
    }
    public GliderFlight getById(Long id) {
        return gliderFlightRepository.findFirstById(id);
    }


}
