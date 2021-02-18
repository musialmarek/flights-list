package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.price.PriceService;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/aircraft")
public class AircraftAddController {
    private final AircraftRepository aircraftRepository;
    private final PriceService priceService;

    @GetMapping("/add")
    public String addForm(Model model) {
        log.debug("\n ADDING EMPTY AIRCRAFT TO MODEL");
        model.addAttribute("aircraft", new Aircraft());
        model.addAttribute("action", "add");
        model.addAttribute("prices", priceService.findAll());
        return "aircrafts/add-edit-aircraft";
    }

    @PostMapping("/add")
    public String addAction(Aircraft aircraft) {
        log.debug("\n{}", aircraft);
        Aircraft saved = aircraftRepository.save(aircraft);
        if (saved.getId() != null) {
            log.debug("\n SUCCESSFUL ADDING AIRCRAFT: {} WITH ID : {}", saved.getRegistrationNumber(), saved.getId());
        }
        return "redirect:/admin/aircrafts";
    }
}
