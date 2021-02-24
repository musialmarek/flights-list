package pl.jgora.aeroklub.airflightslist.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.model.Note;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/notes")
public class NoteController {
    private final PilotService pilotService;
    private final NoteService noteService;

    @ModelAttribute("pilots")
    List<Pilot> getAllPilots() {
        return pilotService.findAll();
    }

    @GetMapping
    public String getAllNotes(Model model, @RequestParam(name = "filter", required = false) Boolean filter,
                              @ModelAttribute(name = "flightsFilter") FlightsFilter flightsFilter) {
        model.addAttribute("category", "admin");
        model.addAttribute("flightsFilter", new FlightsFilter());
        List<Note> notes;
        if (filter != null && filter) {
            notes = noteService.getFilteredNotes(flightsFilter);
        } else {
            notes = noteService.findAll();
        }
        model.addAttribute("notes", notes);
        return "/notes/notes";
    }

    @PostMapping("/deactivate")
    public String deactivate(Note note) {
        Note toDeactivate = noteService.findById(note.getId());
        log.debug("\nNOTE TO DEACTIVATE : {}", toDeactivate.getNumber());
        noteService.activationUpdate(toDeactivate, false);
        log.debug("\nNOTE: {} IS NOT ACTIVE NOW", toDeactivate.getNumber());
        return "redirect:/admin/notes";
    }

    @PostMapping("/activate")
    public String activate(Note note) {
        Note toActivate = noteService.findById(note.getId());
        log.debug("\nNOTE TO ACTIVATE : {}", toActivate.getNumber());
        noteService.activationUpdate(toActivate, true);
        log.debug("\nNOTE: {} IS ACTIVE NOW", toActivate.getNumber());
        return "redirect:/admin/notes";
    }


}
