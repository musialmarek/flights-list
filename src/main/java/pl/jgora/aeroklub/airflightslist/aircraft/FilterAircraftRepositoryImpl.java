package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Transactional
@Slf4j
public class FilterAircraftRepositoryImpl implements FilterAircraftRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Aircraft> filteringAircrafts(String whereSection, Map<String,String> filters) {
        TypedQuery<Aircraft> query = entityManager.createQuery("SELECT a FROM Aircraft a WHERE " + whereSection + "  ORDER BY a.type", Aircraft.class);
        filters.forEach(query::setParameter);
        log.debug("\nQUERY {}", query.toString());
        return query.getResultList();
    }
}
