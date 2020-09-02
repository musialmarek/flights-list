package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/pilot")
public class PilotAddController {
    private final PilotRepository pilotRepository;

    @GetMapping("/add")
    public String addForm(Model model) {
        log.info("\n ADDING EMPTY PILOT TO MODEL");
        model.addAttribute("pilot", new Pilot());
        return "pilots/add-pilot";
    }

    @PostMapping("/add")
    public String addAction(Pilot pilot) {
        log.info("\n{}", pilot);
        Pilot saved = pilotRepository.save(pilot);
        if (saved.getId() != null) {
            log.info("\n SUCCESSFUL ADDING PILOT: {} WITH ID : {}", saved.getName(), saved.getId());
        }
        return "redirect:/admin/pilots";
    }
}
