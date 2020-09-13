package pl.jgora.aeroklub.airflightslist.aircraft;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.List;
import java.util.Set;

public interface AircraftRepository extends JpaRepository<Aircraft, Long>, FilterAircraftRepository {
    List<Aircraft> findAircraftsByOrderByType();

    Aircraft findFirstById(Long id);

    Set<Aircraft> findByEngineTrue();
}
