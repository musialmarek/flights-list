package pl.jgora.aeroklub.airflightslist.AbstractFlight;

import org.slf4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

public class AbstractFlightControllerUtil {
    public static void showDates(Model model, @RequestParam(name = "year", required = false) Integer year, Logger log, Map<LocalDate, Boolean> datesAndActives2) {
        if (year == null) {
            log.debug("\n NOT YEAR PARAMETER - GETTING THIS YEAR ");
            year = LocalDate.now().getYear();
        }
        Map<LocalDate, Boolean> datesAndActives = datesAndActives2;
        for (Map.Entry<LocalDate, Boolean> entry : datesAndActives.entrySet()) {
            log.debug("\ndate {} active {}", entry.getKey().toString(), entry.getValue());
        }
        model.addAttribute("dates", datesAndActives);
    }
}
