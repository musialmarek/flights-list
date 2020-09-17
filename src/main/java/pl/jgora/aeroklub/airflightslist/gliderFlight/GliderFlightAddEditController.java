package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.*;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.time.LocalDate;
import java.util.Set;

@Controller
@RequestMapping("admin/glider-flights")
@RequiredArgsConstructor
@Slf4j
public class GliderFlightAddEditController {
    private final PilotService pilotService;
    private final AircraftService aircraftService;
    private final GliderFlightService gliderFlightService;


    @ModelAttribute("pilots")
    Set<Pilot> getPilots() {
        return pilotService.getGliderPilots();
    }

    @ModelAttribute("instructors")
    Set<Pilot> getInstructors() {
        return pilotService.getGliderInstructors();
    }

    @ModelAttribute("aircrafts")
    Set<Aircraft> getAircrafts() {
        return aircraftService.getGliders();
    }

    @ModelAttribute("towaircrafts")
    Set<Aircraft> getTowAircrafts() {
        return aircraftService.getEngineAircrafts();
    }

    @ModelAttribute("towpilots")
    Set<Pilot> getTowPilots() {
        return pilotService.getTowPilots();
    }

    @ModelAttribute("engineinstructors")
    Set<Pilot> getEngineInstructors() {
        return pilotService.getEngineInstructors();
    }

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam("date") String date, @RequestParam(name = "id", required = false) Long id) {
        GliderFlight flight = new GliderFlight();
        flight.setDate(LocalDate.parse(date));
        if (id != null) {
            flight = gliderFlightService.getById(id);
            flight.setId(null);
        }
        log.debug("ADDING FLIGHT TO MODEL");
        model.addAttribute("flight", flight);
        return "flights/glider-add-flight";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("flight") GliderFlight gliderFlight) {
        String date = gliderFlight.getDate().toString();
        EngineFlight towFlight = gliderFlight.getEngineFlight();
        towFlight.setTow(true);
        towFlight.setTask("HOL");
        towFlight.setStart(gliderFlight.getStart());
        towFlight.setActive(false);
        gliderFlight.setActive(false);
        if (!gliderFlight.getStartMethod().equals(StartMethod.ATTO)) {
            gliderFlight.setEngineFlight(null);
        }
        log.debug("\n GLIDER-FLIGHT BEFORE SAVE: {}", gliderFlight);
        gliderFlightService.save(gliderFlight);
        log.debug("\n GLIDER-FLIGHT AFTER SAVE: {}", gliderFlight);
        return "redirect:/admin/glider-flights/list?date=" + date;
    }

    @GetMapping("/edit")
    public String gliderFlightEditForm(Model model, @RequestParam Long id) {
        GliderFlight toEdit = gliderFlightService.getById(id);
        log.debug("\nADDING EDITING FLIGHT TO MODEL {}", toEdit);
        model.addAttribute("flight", toEdit);
        return "flights/glider-edit-flight";
    }

    @PostMapping("/edit")
    public String gliderFlightEditAction(@ModelAttribute("flight") GliderFlight toEdit) {
        log.debug("\nEDITING FLIGHT WITH ID {}", toEdit.getId());
        toEdit.getEngineFlight().setStart(toEdit.getStart());
        gliderFlightService.update(toEdit);
        String date = toEdit.getDate().toString();
        return "redirect:/admin/glider-flights/list?date=" + date;
    }
}