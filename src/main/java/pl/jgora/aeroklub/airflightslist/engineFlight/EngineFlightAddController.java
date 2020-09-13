package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.time.LocalDate;
import java.util.Set;


@Controller
@RequestMapping("admin/engine-flights")
@RequiredArgsConstructor
@Slf4j
public class EngineFlightAddController {
    private final PilotService pilotService;
    private final AircraftService aircraftService;
    private final EngineFlightService engineFlightService;

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam("date") String date, @RequestParam(name = "id", required = false) Long id) {
        Set<Pilot> engineInstructors = pilotService.getEngineInstructors();
        Set<Pilot> enginePilots = pilotService.getEnginePilots();
        Set<Aircraft> engineAircrafts = aircraftService.getEngineAircrafts();
        log.debug("\n pilots size {}", enginePilots.size());
        model.addAttribute("pilots", enginePilots);
        EngineFlight flight = new EngineFlight();
        flight.setDate(LocalDate.parse(date));
        if (id != null) {
            flight = engineFlightService.getById(id);
            flight.setId(null);
        }
        model.addAttribute("flight", flight);
        model.addAttribute("aircrafts", engineAircrafts);
        model.addAttribute("instructors", engineInstructors);
        return "flights/add-engine-flight";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("flight") EngineFlight engineFlight) {

        String date = engineFlight.getDate().toString();
        engineFlight.setActive(false);
        engineFlight.setTow(false);
        log.debug("\n ENGINE-FLIGHT BEFORE SAVE: {}", engineFlight);
        engineFlightService.save(engineFlight);
        log.debug("\n ENGINE-FLIGHT AFTER SAVE: {}", engineFlight);
        return "redirect:/admin/engine-flights/list?date=" + date;
    }
}
