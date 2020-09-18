package pl.jgora.aeroklub.airflightslist.gliderFlight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jgora.aeroklub.airflightslist.AbstractFlight.FilterFlightsRepository;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface GliderFlightRepository extends JpaRepository<GliderFlight, Long>, FilterFlightsRepository {
    @Query("SELECT e.date FROM GliderFlight e WHERE :start < e.date and e.date < :finish")
    Set<LocalDate> getFlyingGliderDays(LocalDate start, LocalDate finish);

    Set<GliderFlight> getDistinctByDateOrderByStart(LocalDate date);

    GliderFlight findFirstById(Long id);

    List<GliderFlight> findByPicOrCopilotOrPicNameLikeOrCopilotNameLike(Pilot pic, Pilot copilot, String picName, String copilotName);

}
