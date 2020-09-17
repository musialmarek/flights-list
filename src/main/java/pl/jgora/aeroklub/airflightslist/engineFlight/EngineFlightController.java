package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/engine-flights")
public class EngineFlightController {
    private final EngineFlightService engineFlightService;

    @GetMapping
    public String engineFlightsDates(Model model, @RequestParam(name = "year", required = false) Integer year) {
        if (year == null) {
            log.debug("\n NOT YEAR PARAMETER - GETTING THIS YEAR ");
            year = LocalDate.now().getYear();
        }
        Map<LocalDate, Boolean> datesAndActives = engineFlightService.getDatesAndActives(year);
        for (Map.Entry<LocalDate, Boolean> entry : datesAndActives.entrySet()) {
            log.debug("\ndate {} active {}", entry.getKey().toString(), entry.getValue());
        }
        model.addAttribute("dates", datesAndActives);
        return "flights/engine-dates";
    }

    @GetMapping("/list")
    public String engineDailyFlights(Model model, @RequestParam("date") String date) {
        log.debug("\ndate {}", date);
        Set<EngineFlight> flightsInDay = engineFlightService.getByDate(LocalDate.parse(date));
        log.debug("\n flying-list size {}", flightsInDay.size());
        model.addAttribute("date", date);
        model.addAttribute("flights", flightsInDay);
        return "flights/engine-daily";
    }

    @GetMapping("/details")
    public String showFlightDetails(Model model, @RequestParam("id") Long id, @RequestParam("date") String date) {
        log.debug("\n SHOWING DETAILS OF FLIGHT WITH ID: {}", id);
        if (engineFlightService.getById(id) != null) {
            model.addAttribute("flight", engineFlightService.getById(id));
            return "flights/engine-flight-details";
        }
        return engineDailyFlights(model, date);
    }

    @PostMapping("/activate")
    public String activateList(@RequestParam("date") String date) {
        log.debug("\nACTIVATING LIST FROM {}", date);
        Set<EngineFlight> flights = engineFlightService.getByDate(LocalDate.parse(date));
        for (EngineFlight flight : flights) {
            flight.setActive(true);
            engineFlightService.update(flight);
        }
        return "redirect:/admin/engine-flights";
    }

    @PostMapping("/deactivate")
    public String deactivateList(@RequestParam("date") String date) {
        log.debug("\nDEACTIVATING LIST FROM {}", date);
        Set<EngineFlight> flights = engineFlightService.getByDate(LocalDate.parse(date));
        for (EngineFlight flight : flights) {
            flight.setActive(false);
            engineFlightService.update(flight);
        }
        return "redirect:/admin/engine-flights";
    }
}