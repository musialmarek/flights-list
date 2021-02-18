package pl.jgora.aeroklub.airflightslist.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Price;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;
    public List<Price> findAll() {
        return priceRepository.findAll();
    }
}
