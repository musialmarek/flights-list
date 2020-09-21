package pl.jgora.aeroklub.airflightslist.gliderFlight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FilterFlightsRepository;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface GliderFlightRepository extends JpaRepository<GliderFlight, Long>, FilterFlightsRepository {
    @Query("SELECT e.date FROM GliderFlight e WHERE :start < e.date and e.date < :finish order by e.date")
    Set<LocalDate> getFlyingGliderDays(LocalDate start, LocalDate finish);

    Set<GliderFlight> getDistinctByDateOrderByStart(LocalDate date);

    GliderFlight findFirstById(Long id);

    List<GliderFlight> findByPicOrCopilotOrPicNameOrCopilotName(Pilot pic, Pilot copilot, String picName, String copilotName);

    List<GliderFlight> findByAircraftOrAircraftTypeAndAircraftRegistrationNumber(Aircraft aircraft, String type, String registrationNumber);
}
