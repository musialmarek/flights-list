package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.ListSummary;
import pl.jgora.aeroklub.airflightslist.pdfExporter.PdfExporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        model.addAttribute("previousYear", year - 1);
        model.addAttribute("nextYear", year + 1);
        log.debug("\n YEAR: {}", year);
        Map<LocalDate, Boolean> datesAndActives = engineFlightService.getDatesAndActives(year);
        for (Map.Entry<LocalDate, Boolean> entry : datesAndActives.entrySet()) {
            log.debug("\ndate {} active {}", entry.getKey().toString(), entry.getValue());
        }
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("dates", datesAndActives);
        return "flights/engine-dates";
    }

    @GetMapping("/list")
    public String engineDailyFlights(Model model, @RequestParam("date") String date) {
        log.debug("\ndate {}", date);
        Set<EngineFlight> flightsInDay = engineFlightService.getByDate(LocalDate.parse(date));
        Set<AbstractFlight> flights = flightsInDay.stream().map(engineFlight -> (AbstractFlight) engineFlight).collect(Collectors.toSet());
        log.debug("\n flying-list size {}", flightsInDay.size());
        model.addAttribute("date", date);
        model.addAttribute("flights", flightsInDay);
        ListSummary summary = new ListSummary(flights);
        model.addAttribute("summary", summary);
        List<EngineFlight> engineFlights = new ArrayList<>(flightsInDay);
        PdfExporter.addPdfExporterToModel("pdf", model, engineFlights, PdfExporter.ListType.DAILY);
        model.addAttribute("category", "daily");
        model.addAttribute("type", "engine");
        return "flights/list";
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
        String year = date.substring(0, 4);
        return "redirect:/admin/engine-flights?year=" + year;
    }

    @PostMapping("/deactivate")
    public String deactivateList(@RequestParam("date") String date) {
        log.debug("\nDEACTIVATING LIST FROM {}", date);
        Set<EngineFlight> flights = engineFlightService.getByDate(LocalDate.parse(date));
        for (EngineFlight flight : flights) {
            flight.setActive(false);
            engineFlightService.update(flight);
        }
        String year = date.substring(0, 4);
        return "redirect:/admin/engine-flights?year=" + year;
    }
}