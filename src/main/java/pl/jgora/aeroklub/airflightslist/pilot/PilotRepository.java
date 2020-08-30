package pl.jgora.aeroklub.airflightslist.pilot;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

public interface PilotRepository extends JpaRepository<Pilot,Long> {
}
