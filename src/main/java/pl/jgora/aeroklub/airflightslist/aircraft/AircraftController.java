package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/aircrafts")
public class AircraftController {
    private final AircraftService aircraftService;

    @GetMapping
    public String showAllAircrafts(Model model,
                                   @RequestParam(value = "filter", required = false) Boolean filter,
                                   @ModelAttribute(name = "aircraftFilter") AircraftFilter aircraftFilter) {
        List<Aircraft> aircrafts;
        model.addAttribute("aircraftFilter", new AircraftFilter());
        if (filter != null) {
            log.debug("\n FILTER IS TRUE");
            aircrafts = aircraftService.filteredAircrafts(aircraftFilter);
        } else {
            aircrafts = aircraftService.findAll();
        }
        log.debug("Got list of aircrafts with size: {} \n Adding list of aircrafts to model as \"aircrafts\" ", aircrafts.size());
        model.addAttribute("aircrafts", aircrafts);
        log.debug("List of aircrafts:");
        for (Aircraft aircraft : aircrafts) {
            log.debug(" Aircraft {}", aircraft);
        }
        return "aircrafts/aircrafts";
    }

    @PostMapping("/deactivate")
    public String deactivate(Aircraft aircraft) {
        Aircraft toDeactivate = aircraftService.findById(aircraft.getId());
        log.debug("\nAIRCRAFT TO DEACTIVATE : {}", toDeactivate.getRegistrationNumber());
        aircraftService.activationUpdate(toDeactivate, false);
        log.debug("\nAIRCRAFT: {} IS NOT ACTIVE NOW", toDeactivate.getRegistrationNumber());
        return "redirect:/admin/aircrafts";
    }

    @PostMapping("/activate")
    public String activate(Aircraft aircraft) {
        Aircraft toActivate = aircraftService.findById(aircraft.getId());
        log.debug("\nAIRCRAFT TO ACTIVATE : {}", toActivate.getRegistrationNumber());
        aircraftService.activationUpdate(toActivate, true);
        log.debug("\nAIRCRAFT: {} IS ACTIVE NOW", toActivate.getRegistrationNumber());
        return "redirect:/admin/aircrafts";
    }

    @GetMapping("/details")
    public String details(Model model, @RequestParam("id") Long id) {
        Aircraft aircraft = aircraftService.findById(id);
        if (aircraft != null) {
            model.addAttribute("aircraft", aircraft);
            return "aircrafts/aircraft-details";
        }
        return "redirect:/admin/aircrafts";
    }
}
