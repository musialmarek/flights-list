package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngineFlightService {
    private final EngineFlightRepository engineFlightRepository;

    public Set<LocalDate> getAllFlyingDays(int year) {
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
}
