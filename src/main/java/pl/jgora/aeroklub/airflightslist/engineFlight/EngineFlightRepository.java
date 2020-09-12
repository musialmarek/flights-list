package pl.jgora.aeroklub.airflightslist.engineFlight;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

public interface EngineFlightRepository extends JpaRepository<EngineFlight, Long> {

}
