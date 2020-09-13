package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Set<LocalDate> allFlyingDays;
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        allFlyingDays = gliderFlightService.getAllFlyingDays(year);
        model.addAttribute("dates", allFlyingDays);
        return "flights/glider-dates";
    }

    @GetMapping("/list")
    public String gliderDailyFlights(@RequestParam("date") String date, Model model) {
        log.info("\ndate {}", date);
        Set<GliderFlight> flightsInDay = gliderFlightService.getByDate(LocalDate.parse(date));
        log.info("\n flying-list size {}", flightsInDay.size());
        model.addAttribute("date", date);
        model.addAttribute("flights", flightsInDay);
        return "flights/glider-daily";
    }
}
