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

@Controller
@RequestMapping("/admin/aircraft")
@RequiredArgsConstructor
@Slf4j
public class AircraftEditController {
    private final AircraftService aircraftService;

    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam("id") Long id) {
        Aircraft toEdit = aircraftService.findById(id);
        if (toEdit != null) {
            log.info("\n ADDING AIRCRAFT: {} TO MODEL", toEdit.getRegistrationNumber());
            model.addAttribute("aircraft", toEdit);
            return "aircrafts/edit-aircraft";
        }
        log.info("\nTHERE IS NO AIRCRAFT WITH ID: {}", id);
        return "redirect:/admin/aircrafts";
    }

    @PostMapping("/edit")
    public String editAction(Aircraft aircraft) {
        log.info("\n{}", aircraft);
        aircraftService.update(aircraft);
        log.info("\n SUCCESSFUL EDITING OF AIRCRAFT: {}", aircraft.getRegistrationNumber());
        return "redirect:/admin/aircrafts";
    }

}
