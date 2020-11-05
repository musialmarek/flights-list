package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/pilots")
public class PilotController {
    private final PilotService pilotService;

    @GetMapping
    public String showAllPilots(Model model,
                                @RequestParam(value = "filter", required = false) Boolean filter,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String licence,
                                @RequestParam(required = false) Boolean active,
                                @RequestParam(required = false) Boolean glider,
                                @RequestParam(required = false) Boolean fis,
                                @RequestParam(required = false) Boolean engine,
                                @RequestParam(required = false) Boolean fia,
                                @RequestParam(required = false) Boolean tow) {
        List<Pilot> pilots;
        if (filter != null) {
            log.debug("\n FILTER IS TRUE");
            pilots = pilotService.filteredPilots(name, licence, active, glider, fis, engine, fia, tow);
        } else {
            pilots = pilotService.findAll();
        }
        log.debug("Got list of pilots with size: {} \n Adding list of pilots to model as \"pilots\" ", pilots.size());
        model.addAttribute("pilots", pilots);
        log.debug("List of pilots:");
        for (Pilot pilot : pilots) {
            log.debug(" Pilot {}", pilot);
        }
        return "pilots/pilots";
    }

    @PostMapping("/deactivate")
    public String deactivate(Pilot pilot) {
        Pilot toDeactivate = pilotService.findById(pilot.getId());
        log.debug("\nPILOT TO DEACTIVATE : {}", toDeactivate.getName());
        pilotService.activationUpdate(toDeactivate, false);
        log.debug("\nPILOT: {} IS NOT ACTIVE NOW", toDeactivate.getName());
        return "redirect:/admin/pilots";
    }

    @PostMapping("/activate")
    public String activate(Pilot pilot) {
        Pilot toActivate = pilotService.findById(pilot.getId());
        log.debug("\nPILOT TO ACTIVATE : {}", toActivate.getName());
        pilotService.activationUpdate(toActivate, true);
        log.debug("\nPILOT: {} IS ACTIVE NOW", toActivate.getName());
        return "redirect:/admin/pilots";
    }

    @GetMapping("/details")
    public String details(Model model, @RequestParam("id") Long id) {
        Pilot pilot = pilotService.findById(id);
        if (pilot != null) {
            model.addAttribute("pilot", pilot);
            return "pilots/pilot-details";
        }
        return "redirect:/admin/pilots";
    }
}
