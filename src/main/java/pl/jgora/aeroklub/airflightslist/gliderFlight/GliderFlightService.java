package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GliderFlightService {
    private final GliderFlightRepository gliderFlightRepository;

    public Set<LocalDate> getAllFlyingDays(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate finish = LocalDate.of(year, 12, 31);
        return gliderFlightRepository.getFlyingGliderDays(start, finish);
    }

    public Set<GliderFlight> getByDate(LocalDate date) {
        return gliderFlightRepository.getDistinctByDateOrderByStart(date);
    }
}
