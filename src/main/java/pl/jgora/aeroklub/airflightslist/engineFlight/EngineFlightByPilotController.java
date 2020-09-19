package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EngineFlightByPilotController {
    private final EngineFlightService engineFlightService;
    private final PilotService pilotService;
    private final AircraftService aircraftService;

    @GetMapping("admin/pilot/engine-flights")
    public String showPilotsEngineFlights(
            Model model,
            @RequestParam("id") Long id,
            @RequestParam(name = "filter", required = false) Boolean filter,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "task", required = false) String task,
            @RequestParam(name = "pic", required = false) Boolean pic,
            @RequestParam(name = "instructor", required = false) Boolean instructor,
            @RequestParam(name = "aircraft", required = false) Long aircraft,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "registration", required = false) String registration
    ) {
        log.debug("\nGETTING PILOT WITH ID {} ", id);
        Pilot pilot = pilotService.findById(id);
        StringBuilder sb = new StringBuilder();
        List<EngineFlight> flights = new ArrayList<>();
        if (pilot != null) {

            if (filter != null) {
                log.debug("\n FILTER IS TRUE");
                Aircraft engineAircraft = aircraftService.findById(aircraft);
                flights = engineFlightService.getFilteredEngineFlightsByPilot(pilot, active, from, to, task, pic, instructor, engineAircraft, type, registration);

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
        return "flights/engine-by-pilot";
    }

}
