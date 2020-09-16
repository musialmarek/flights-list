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
public class EngineFlightAddEditController {
    private final PilotService pilotService;
    private final AircraftService aircraftService;
    private final EngineFlightService engineFlightService;

    @ModelAttribute("pilots")
    Set<Pilot> getPilots() {
        return pilotService.getEnginePilots();
    }

    @ModelAttribute("instructors")
    Set<Pilot> getInstructors() {
        return pilotService.getEngineInstructors();
    }

    @ModelAttribute("aircrafts")
    Set<Aircraft> getAircrafts() {
        return aircraftService.getEngineAircrafts();
    }

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam("date") String date, @RequestParam(name = "id", required = false) Long id) {
        EngineFlight flight = new EngineFlight();
        flight.setDate(LocalDate.parse(date));
        if (id != null) {
            flight = engineFlightService.getById(id);
            flight.setId(null);
        }
        model.addAttribute("flight", flight);
        return "flights/add-engine-flight";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("flight") EngineFlight engineFlight) {
        String date = engineFlight.getDate().toString();
        log.debug("\n ENGINE-FLIGHT BEFORE SAVE: {}", engineFlight);
        engineFlightService.save(engineFlight);
        log.debug("\n ENGINE-FLIGHT AFTER SAVE: {}", engineFlight);
        return "redirect:/admin/engine-flights/list?date=" + date;
    }

    @GetMapping("/edit")
    public String engineFlightEditForm(Model model, @RequestParam Long id) {
        EngineFlight toEdit = engineFlightService.getById(id);
        log.debug("\nADDING EDITING FLIGHT TO MODEL {}", toEdit);
        model.addAttribute("flight", toEdit);
        return "flights/edit-engine-flight";
    }

    @PostMapping("/edit")
    public String engineFlightEditAction(@ModelAttribute("flight") EngineFlight toEdit) {
        log.debug("\nEDITING FLIGHT WITH ID {}", toEdit.getId());
        engineFlightService.update(toEdit);
        String date = toEdit.getDate().toString();
        return "redirect:/admin/engine-flights/list?date=" + date;
    }
}
