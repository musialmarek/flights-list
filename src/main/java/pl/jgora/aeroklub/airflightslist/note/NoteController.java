package pl.jgora.aeroklub.airflightslist.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.model.Note;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NoteController {
    private final PilotService pilotService;
    private final NoteService noteService;

    @ModelAttribute("pilots")
    List<Pilot> getAllPilots() {
        return pilotService.findAll();
    }

    @GetMapping("admin/notes")
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


}
