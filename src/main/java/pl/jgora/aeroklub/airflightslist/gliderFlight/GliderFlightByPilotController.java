package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.ListSummary;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;
import pl.jgora.aeroklub.airflightslist.user.CurrentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class GliderFlightByPilotController {
    private final GliderFlightService gliderFlightService;
    private final PilotService pilotService;
    private final AircraftService aircraftService;

    @GetMapping("/user/glider-flights")
    public String showUsersGliderFlights(@AuthenticationPrincipal CurrentUser user,
                                         Model model,
                                         @RequestParam(value = "filter", required = false) Boolean filter,
                                         @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter) {

        Pilot pilot = user.getUser().getPilot();
        flightsFilter.setPilot(pilot);
        model.addAttribute("flightsFilter", new FlightsFilter());
        showGliderFlights(model, filter, flightsFilter);
        return "flights/glider-by-user";

    }

    @GetMapping("admin/pilot/glider-flights")
    public String showPilotsGliderFlights(Model model,
                                          @RequestParam(value = "id") Long id,
                                          @RequestParam(value = "filter", required = false) Boolean filter,
                                          @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter) {
        Pilot pilot = pilotService.findById(id);
        flightsFilter.setPilot(pilot);
        showGliderFlights(model, filter, flightsFilter);
        return "flights/glider-by-pilot";
    }

    private void showGliderFlights(Model model, Boolean filter, FlightsFilter flightsFilter) {
        log.debug("\nGETTING PILOT {} ", flightsFilter.getPilot());
        Pilot pilot = flightsFilter.getPilot();
        StringBuilder sb = new StringBuilder();
        List<GliderFlight> flights = new ArrayList<>();
        if (pilot != null) {
            if (filter != null) {
                log.debug("\n FILTER IS TRUE");
                flights = gliderFlightService.getFilteredGliderFlightsByPilot(flightsFilter);
            } else {
                flights = gliderFlightService.getByPilot(pilot);
            }

            for (GliderFlight flight : flights) {
                sb.append("\nPilot: ").append(pilot.getName()).append(" FLIGHT: ").append(flight);
            }
            log.debug("\n{}", sb.toString());
        }
        model.addAttribute("flights", flights);
        model.addAttribute("pilot", pilot);
        model.addAttribute("aircrafts", aircraftService.getGliders());
        model.addAttribute("summary", new ListSummary(flights
                .stream()
                .map(flight -> (AbstractFlight) flight)
                .collect(Collectors.toSet())));
    }
}
