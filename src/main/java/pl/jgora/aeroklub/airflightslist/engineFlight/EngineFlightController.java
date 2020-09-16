package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import java.time.LocalDate;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/engine-flights")
public class EngineFlightController {
    private final EngineFlightService engineFlightService;

    @GetMapping
    public String engineFlightsDates(Model model, @RequestParam(name = "year", required = false) Integer year) {
        Set<LocalDate> allFlyingDays;
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        allFlyingDays = engineFlightService.getAllFlyingDays(year);
        model.addAttribute("dates", allFlyingDays);
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
}
