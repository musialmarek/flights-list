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
import pl.jgora.aeroklub.airflightslist.price.PriceService;

@Controller
@RequestMapping("/admin/aircraft")
@RequiredArgsConstructor
@Slf4j
public class AircraftEditController {
    private final AircraftService aircraftService;
    private final PriceService priceService;

    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam("id") Long id) {
        Aircraft toEdit = aircraftService.findById(id);
        if (toEdit != null) {
            log.debug("\n ADDING AIRCRAFT: {} TO MODEL", toEdit.getRegistrationNumber());
            model.addAttribute("aircraft", toEdit);
            model.addAttribute("action", "edit");
            model.addAttribute("prices", priceService.findAll());
            return "aircrafts/add-edit-aircraft";
        }
        log.debug("\nTHERE IS NO AIRCRAFT WITH ID: {}", id);
        return "redirect:/admin/aircrafts";
    }

    @PostMapping("/edit")
    public String editAction(Aircraft aircraft) {
        log.debug("\n{}", aircraft);
        aircraftService.update(aircraft);
        log.debug("\n SUCCESSFUL EDITING OF AIRCRAFT: {}", aircraft.getRegistrationNumber());
        return "redirect:/admin/aircrafts";
    }

}
