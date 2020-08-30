package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PilotController {
    private final PilotService pilotService;

    @GetMapping
    public String showAllPilots(Model model) {
        List<Pilot> pilots = pilotService.findAll();
        model.addAttribute("size", pilots.size());
        log.error("cokolwiek size:{}", pilots.size());
        model.addAttribute("pilots", pilots);
        model.addAttribute("welcome", "WELCOME");
        for (Pilot pilot : pilots) {
            log.error("{}", pilot);
        }
        return "pilots/all";
    }
}
