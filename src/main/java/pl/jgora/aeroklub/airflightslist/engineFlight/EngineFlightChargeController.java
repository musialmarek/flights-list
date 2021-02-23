package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.note.NoteService;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("admin/engine-flights/charge")
public class EngineFlightChargeController {
    private final EngineFlightService engineFlightService;
    private final PilotService pilotService;
    private final NoteService noteService;

    @ModelAttribute("type")
    public String getType() {
        return "engine";
    }

    @GetMapping()
    public String showFlightsToCharge(Model model, @RequestParam(name = "id") Long id) {
        Pilot pilot = pilotService.findById(id);
        model.addAttribute("pilot", pilot);
        List<EngineFlight> flights = engineFlightService.getAllToChargeByPayer(pilot);
        model.addAttribute("flights", flights);
        log.debug("flights to charge {}", flights.size());
        log.debug("pilot payer {}", pilot.getName());
        model.addAttribute("category", "charge");
        return "flights/list";
    }

    @PostMapping()
    public String chargeFlights(Model model, @RequestParam(name = "id") Long id, HttpServletRequest request) {
        Pilot pilot = pilotService.findById(id);
        model.addAttribute("pilot", pilot);
        List<EngineFlight> flights = engineFlightService.getAllToChargeByPayer(pilot);
        List<EngineFlight> flightsToCharge = new ArrayList<>();
        log.debug("i'm in post method");
        for (EngineFlight flight : flights) {
            if (request.getParameter(flight.getId().toString()) != null) {
                flightsToCharge.add(flight);
            }
        }
        noteService.createNote(flightsToCharge);
        return "redirect:/admin/engine-flights/charge?id=" + pilot.getId();
    }
}
