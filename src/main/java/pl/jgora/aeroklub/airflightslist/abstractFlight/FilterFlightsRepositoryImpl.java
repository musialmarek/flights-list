package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Slf4j
public class FilterFlightsRepositoryImpl implements FilterFlightsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EngineFlight> getFilteredEngineFlights(FlightsFilter flightsFilter) {
        log.debug("\nCREATING QUERY to FILTER ENGINE FLIGHTS BY PILOT");
        TypedQuery<EngineFlight> query = entityManager.createQuery("SELECT f FROM EngineFlight f WHERE " + flightsFilter.getWhereSection() + "  ORDER BY f.date, f.start ", EngineFlight.class);
        log.debug("\nSETTING PARAMETERS TO QUERY");
        flightsFilter.getFilters().forEach(query::setParameter);
        log.debug("\nQUERY {}", query.toString());
        return query.getResultList();
    }

    @Override
    public List<GliderFlight> getFilteredGliderFlights(FlightsFilter flightsFilter) {
        log.debug("\nCREATING QUERY to FILTER GLIDER FLIGHTS BY PILOT");
        TypedQuery<GliderFlight> query = entityManager.createQuery("SELECT f FROM GliderFlight f WHERE " + flightsFilter.getWhereSection() + "  ORDER BY f.date, f.start ", GliderFlight.class);
        log.debug("\nSETTING PARAMETERS TO QUERY");
        flightsFilter.getFilters().forEach(query::setParameter);
        log.debug("\nQUERY {}", query.toString());
        return query.getResultList();
    }
}
