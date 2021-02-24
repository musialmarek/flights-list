package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
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

    @ModelAttribute("type")
    String getType() {
        return "engine";
    }

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam("date") String date, @RequestParam(name = "id", required = false) Long id) {
        EngineFlight flight = new EngineFlight();
        flight.setDate(LocalDate.parse(date));
        if (id != null) {
            flight = engineFlightService.getById(id);
            AbstractFlightService.replacePilots(flight);
            flight.setId(null);
        } else {
            flight.setCharge(true);
        }
        log.debug("ADDING FLIGHT TO MODEL");
        model.addAttribute("flight", flight);
        model.addAttribute("action", "add");
        return "flights/add-edit-flight";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("flight") EngineFlight engineFlight) {
        String date = engineFlight.getDate().toString();
        engineFlight.setTow(false);
        engineFlight.setActive(false);
        log.debug("\n ENGINE-FLIGHT BEFORE SAVE: {}", engineFlight);
        engineFlightService.save(engineFlight);
        log.debug("\n ENGINE-FLIGHT AFTER SAVE: {}", engineFlight);
        return "redirect:/admin/engine-flights/list?date=" + date;
    }

    @GetMapping("/edit")
    public String engineFlightEditForm(Model model, @RequestParam Long id) {
        EngineFlight toEdit = engineFlightService.getById(id);
        log.debug("\nADDING EDITING FLIGHT TO MODEL {}", toEdit);
        AbstractFlightService.replacePilots(toEdit);
        model.addAttribute("flight", toEdit);
        model.addAttribute("action", "edit");
        return "flights/add-edit-flight";
    }

    @PostMapping("/edit")
    public String engineFlightEditAction(@ModelAttribute("flight") EngineFlight toEdit) {
        log.debug("\nEDITING FLIGHT WITH ID {}", toEdit.getId());
        engineFlightService.update(toEdit);
        String date = toEdit.getDate().toString();
        return "redirect:/admin/engine-flights/list?date=" + date;
    }
}
