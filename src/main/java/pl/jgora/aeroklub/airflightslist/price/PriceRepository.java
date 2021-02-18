package pl.jgora.aeroklub.airflightslist.price;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Price;

public interface PriceRepository extends JpaRepository<Price,Long> {
}
