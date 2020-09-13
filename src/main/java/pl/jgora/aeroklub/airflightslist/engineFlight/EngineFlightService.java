package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

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
}
