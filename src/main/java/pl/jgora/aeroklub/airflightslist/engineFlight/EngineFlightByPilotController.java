package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.ListSummary;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pdfExporter.PdfExporter;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;
import pl.jgora.aeroklub.airflightslist.user.CurrentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EngineFlightByPilotController {
    private final EngineFlightService engineFlightService;
    private final PilotService pilotService;
    private final AircraftService aircraftService;

    @GetMapping("user/engine-flights")
    public String showUsersEngineFlights(@AuthenticationPrincipal CurrentUser user,
                                         Model model,
                                         @RequestParam(name = "filter", required = false) Boolean filter,
                                         @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter) {
        model.addAttribute("category", "user");
        Pilot pilot = user.getUser().getPilot();
        flightsFilter.setPilot(pilot);
        flightsFilter.setActive(true);
        showEngineFlights(model, true, flightsFilter, PdfExporter.ListType.USER);
        return "flights/list";
    }

    @GetMapping("admin/pilot/engine-flights")
    public String showPilotsEngineFlights(
            Model model,
            @RequestParam("id") Long id,
            @RequestParam(name = "filter", required = false) Boolean filter,
            @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter
    ) {
        model.addAttribute("category", "pilot");
        Pilot pilot = pilotService.findById(id);
        flightsFilter.setPilot(pilot);
        showEngineFlights(model, filter, flightsFilter, PdfExporter.ListType.PILOT);
        return "flights/list";
    }

    private void showEngineFlights(Model model, Boolean filter, FlightsFilter flightsFilter, PdfExporter.ListType type) {
        log.debug("\nGETTING PILOT  {}", flightsFilter.getPilot());
        Pilot pilot = flightsFilter.getPilot();
        StringBuilder sb = new StringBuilder();
        List<EngineFlight> flights = new ArrayList<>();
        if (pilot != null) {

            if (filter != null) {
                log.debug("\n FILTER IS TRUE");
                flights = engineFlightService.getFilteredEngineFlightsByPilot(flightsFilter);

            } else {
                flights = engineFlightService.getByPilot(pilot);
            }
            for (EngineFlight flight : flights) {
                sb.append("\nPilot: ").append(pilot.getName()).append(" FLIGHT: ").append(flight);
            }
            log.debug("\n{}", sb.toString());
        }
        log.debug("\n ADDING FLIGHTS LIST SIZE {} TO MODEL AS \"flights\"", flights.size());
        model.addAttribute("flights", flights);
        model.addAttribute("pilot", pilot);
        model.addAttribute("aircrafts", aircraftService.getEngineAircrafts());
        model.addAttribute("summary", new ListSummary(flights
                .stream()
                .map(flight -> (AbstractFlight) flight)
                .collect(Collectors.toSet())));
        PdfExporter.addPdfExporterToModel("pdf", model, flights, type);
        model.addAttribute("type", "engine");
        model.addAttribute("flightsFilter", new FlightsFilter());
    }
}
