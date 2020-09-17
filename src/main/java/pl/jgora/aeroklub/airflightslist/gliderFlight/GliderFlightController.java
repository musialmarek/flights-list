package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.AbstractFlight.AbstractFlightControllerUtil;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import java.time.LocalDate;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/glider-flights")
public class GliderFlightController {
    private final GliderFlightService gliderFlightService;

    @GetMapping
    public String gliderFlightsDates(Model model, @RequestParam(name = "year", required = false) Integer year) {
        AbstractFlightControllerUtil.showDates(model, year, log, gliderFlightService.getDatesAndActives(year));
        return "flights/glider-dates";

    }

    @GetMapping("/list")
    public String gliderDailyFlights(Model model, @RequestParam("date") String date) {
        log.debug("\ndate {}", date);
        Set<GliderFlight> flightsInDay = gliderFlightService.getByDate(LocalDate.parse(date));
        log.debug("\n flying-list size {}", flightsInDay.size());
        model.addAttribute("date", date);
        model.addAttribute("flights", flightsInDay);
        return "flights/glider-daily";
    }

    @GetMapping("/details")
    public String showFlightDetails(Model model, @RequestParam("id") Long id, @RequestParam("date") String date) {
        log.debug("\n SHOWING DETAILS OF FLIGHT WITH ID: {}", id);
        if (gliderFlightService.getById(id) != null) {
            model.addAttribute("flight", gliderFlightService.getById(id));
            return "flights/glider-flight-details";
        }
        return gliderDailyFlights(model, date);
    }

    @PostMapping("/activate")
    public String activateList(@RequestParam("date") String date) {
        log.debug("\nACTIVATING LIST FROM {}", date);
        Set<GliderFlight> flights = gliderFlightService.getByDate(LocalDate.parse(date));
        for (GliderFlight flight : flights) {
            flight.setActive(true);
            flight.getEngineFlight().setActive(true);
            gliderFlightService.update(flight);
        }
        return "redirect:/admin/glider-flights";
    }

    @PostMapping("/deactivate")
    public String deactivateList(@RequestParam("date") String date) {
        log.debug("\nDEACTIVATING LIST FROM {}", date);
        Set<GliderFlight> flights = gliderFlightService.getByDate(LocalDate.parse(date));
        for (GliderFlight flight : flights) {
            flight.setActive(false);
            flight.getEngineFlight().setActive(true);
            gliderFlightService.update(flight);
        }
        return "redirect:/admin/glider-flights";
    }
}
