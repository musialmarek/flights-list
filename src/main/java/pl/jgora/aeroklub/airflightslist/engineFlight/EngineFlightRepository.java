package pl.jgora.aeroklub.airflightslist.engineFlight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jgora.aeroklub.airflightslist.AbstractFlight.FilterFlightsRepository;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface EngineFlightRepository extends JpaRepository<EngineFlight, Long>, FilterFlightsRepository {
    @Query("SELECT e.date FROM EngineFlight e WHERE :start < e.date and e.date < :finish and e.tow = false or e.tow is null")
    Set<LocalDate> getFlyingEngineDays(LocalDate start, LocalDate finish);

    Set<EngineFlight> getDistinctByDateOrderByStart(LocalDate date);

    EngineFlight findFirstById(Long id);

    List<EngineFlight> findByPicOrCopilotOrPicNameLikeOrCopilotNameLike(Pilot pic,Pilot copilot,String picName, String copilotName);
}
