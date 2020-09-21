package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;
import pl.jgora.aeroklub.airflightslist.user.CurrentUser;

import java.util.ArrayList;
import java.util.List;

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
                                         @RequestParam(name = "filter", required = false) Boolean filter,
                                         @RequestParam(name = "from", required = false) String from,
                                         @RequestParam(name = "to", required = false) String to,
                                         @RequestParam(name = "task", required = false) String task,
                                         @RequestParam(name = "pic", required = false) Boolean pic,
                                         @RequestParam(name = "instructor", required = false) Boolean instructor,
                                         @RequestParam(name = "aircraft", required = false) Long aircraft,
                                         @RequestParam(name = "type", required = false) String type,
                                         @RequestParam(name = "registration", required = false) String registration,
                                         @RequestParam(name = "start", required = false) String start
    ) {
        Long id = user.getUser().getPilot().getId();
        showGliderFlights(model, id, filter, true, from, to, task, pic, instructor, aircraft, type, registration, start);
        return "flights/glider-by-user";

    }

    @GetMapping("admin/pilot/glider-flights")
    public String showPilotsGliderFlights(
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
            @RequestParam(name = "registration", required = false) String registration,
            @RequestParam(name = "start", required = false) String start
    ) {
        showGliderFlights(model, id, filter, active, from, to, task, pic, instructor, aircraft, type, registration, start);
        return "flights/glider-by-pilot";
    }

    void showGliderFlights(Model model, @RequestParam("id") Long id, @RequestParam(name = "filter", required = false) Boolean filter, @RequestParam(name = "active", required = false) Boolean active, @RequestParam(name = "from", required = false) String from, @RequestParam(name = "to", required = false) String to, @RequestParam(name = "task", required = false) String task, @RequestParam(name = "pic", required = false) Boolean pic, @RequestParam(name = "instructor", required = false) Boolean instructor, @RequestParam(name = "aircraft", required = false) Long aircraft, @RequestParam(name = "type", required = false) String type, @RequestParam(name = "registration", required = false) String registration, @RequestParam(name = "start", required = false) String start) {
        log.debug("\nGETTING PILOT WITH ID {} ", id);
        Pilot pilot = pilotService.findById(id);
        StringBuilder sb = new StringBuilder();
        List<GliderFlight> flights = new ArrayList<>();
        if (pilot != null) {

            if (filter != null) {
                log.debug("\n FILTER IS TRUE");
                Aircraft gliderAircraft = aircraftService.findById(aircraft);
                flights = gliderFlightService.getFilteredGliderFlightsByPilot(pilot, active, from, to, task, pic, instructor, gliderAircraft, type, registration, start);

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
    }

}
