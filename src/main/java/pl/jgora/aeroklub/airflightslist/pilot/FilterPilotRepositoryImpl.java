package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Transactional
@Slf4j
public class FilterPilotRepositoryImpl implements FilterPilotRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Pilot> getFilteredPilots(String whereSection, Map<String,String> filters) {
        TypedQuery<Pilot> query = entityManager.createQuery("SELECT p FROM Pilot p WHERE " + whereSection + "  ORDER BY p.name", Pilot.class);
        filters.forEach(query::setParameter);
        log.debug("\nQUERY {}", query.toString());
        return query.getResultList();
    }
}
