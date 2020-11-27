package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.gliderFlight.GliderFlightService;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AbstractFlightByAircraftController {
    private final EngineFlightService engineFlightService;
    private final GliderFlightService gliderFlightService;
    private final AircraftService aircraftService;

    @GetMapping("admin/aircraft/flights")

    public String showFlightsByEngine(
            Model model,
            @RequestParam("id") Long id,
            @RequestParam(name = "filter", required = false) Boolean filter,
            @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter
    ) {
        model.addAttribute("flightsFilter", new FlightsFilter());
        log.debug("\nGETTING AIRCRAFT WITH ID {} ", id);
        Aircraft aircraft = aircraftService.findById(id);
        StringBuilder sb = new StringBuilder();
        if (aircraft != null) {
            model.addAttribute("aircraft", aircraft);
            flightsFilter.setAircraft(aircraft);
            if (aircraft.getEngine()) {
                List<EngineFlight> flights;
                if (filter != null && filter == true) {
                    log.debug("\n FILTER IS TRUE");
                    flights = engineFlightService.getFilteredFlightsByAircraft(flightsFilter);

                } else {
                    flights = engineFlightService.getAllByAircraft(aircraft);
                }
                for (EngineFlight flight : flights) {
                    sb.append("\nPilot: ").append(aircraft.getType()).append(aircraft.getRegistrationNumber()).append(" FLIGHT: ").append(flight);
                }
                log.debug("\n{}", sb.toString());
                log.debug("\n ADDING FLIGHTS LIST SIZE {} TO MODEL AS \"flights\"", flights.size());
                model.addAttribute("flights", flights);
                return "flights/engine-by-aircraft";
            } else {
                List<GliderFlight> flights;
                if (filter != null && filter == true) {
                    log.debug("\n FILTER IS TRUE");
                    flights = gliderFlightService.getFilteredFlightsByAircraft(flightsFilter);

                } else {
                    flights = gliderFlightService.getAllByAircraft(aircraft);
                }
                for (GliderFlight flight : flights) {
                    sb.append("\nAIRCRAFT: ").append(aircraft.getType()).append(aircraft.getRegistrationNumber()).append(" FLIGHT: ").append(flight);
                }
                log.debug("\n{}", sb.toString());
                log.debug("\n ADDING FLIGHTS LIST SIZE {} TO MODEL AS \"flights\"", flights.size());
                model.addAttribute("flights", flights);
                return "flights/glider-by-aircraft";
            }
        }
        return "redirect:/admin/aircrafts";
    }
}
