package pl.jgora.aeroklub.airflightslist.price;

import org.springframework.transaction.annotation.Transactional;
import pl.jgora.aeroklub.airflightslist.model.Price;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Transactional
public class TowPriceRepository {
    @PersistenceContext
    static EntityManager entityManager;

    public static Price getTowingPrice() {
        TypedQuery<Price> query = entityManager.createQuery("SELECT p FROM Price p WHERE p.name = \"HOL\"", Price.class);
        return query.getSingleResult();
    }
}
