package pl.jgora.aeroklub.airflightslist.pilot;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;
import java.util.Set;

public interface PilotRepository extends JpaRepository<Pilot, Long>, FilterPilotRepository {
    List<Pilot> findPilotsByOrderByName();

    Pilot findFirstById(Long id);

    Set<Pilot> findByEnginePilotTrueAndActiveTrueOrderByName();

    Set<Pilot> findByEngineInstructorTrueAndActiveTrueOrderByName();

    Set<Pilot> findByTowTrueAndActiveTrueOrderByName();

    Set<Pilot> findByGliderInstructorTrueAndActiveTrueOrderByName();

    Set<Pilot> findByGliderPilotTrueAndActiveTrueOrderByName();
}
