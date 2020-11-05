package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String registration,
                                   @RequestParam(required = false) Boolean active,
                                   @RequestParam(required = false) Boolean engine) {
        List<Aircraft> aircrafts;
        if (filter != null) {
            log.debug("\n FILTER IS TRUE");
            aircrafts = aircraftService.filteredAircrafts(type, registration, active, engine);
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
