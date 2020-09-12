package pl.jgora.aeroklub.airflightslist.engineFlight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import java.time.LocalDate;
import java.util.Set;

public interface EngineFlightRepository extends JpaRepository<EngineFlight, Long> {
    @Query("SELECT e.date FROM EngineFlight e WHERE e.tow =false or e.tow is null and :start < e.date and e.date < :finish")
    Set<LocalDate> getFlyingEngineDays(LocalDate start, LocalDate finish);
}
