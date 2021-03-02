package pl.jgora.aeroklub.airflightslist.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.account.AccountRepository;
import pl.jgora.aeroklub.airflightslist.model.Account;
import pl.jgora.aeroklub.airflightslist.model.Note;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("admin/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteAddEditController {
    private final PilotService pilotService;
    private final NoteService noteService;
    private final AccountRepository accountRepository;

    @ModelAttribute("pilots")
    List<Pilot> getPilots() {
        return pilotService.findAll();
    }

    @ModelAttribute("accounts")
    List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        Note note = new Note();
        note.setDate(LocalDate.now());
        if (id != null) {
            if (noteService.findById(id) != null) {
                note = noteService.findById(id);
                note.setId(null);
            }
        }
        log.debug("ADDING NOTE TO MODEL");
        model.addAttribute("note", note);
        model.addAttribute("action", "add");
        return "notes/add-edit-note";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("note") Note note) {
        log.debug("\n NOTE BEFORE SAVE: {}", note);
        noteService.save(note);
        log.debug("\n NOTE AFTER SAVE: {}", note);
        return "redirect:/admin/notes";
    }

    @GetMapping("/edit")
    public String engineFlightEditForm(Model model, @RequestParam Long id) {
        Note toEdit = noteService.findById(id);
        log.debug("\nADDING EDITING NOTE TO MODEL {}", toEdit);
        model.addAttribute("note", toEdit);
        model.addAttribute("action", "edit");
        return "notes/add-edit-note";
    }

    @PostMapping("/edit")
    public String engineFlightEditAction(@ModelAttribute("flight") Note toEdit) {
        log.debug("\nEDITING NOTE WITH ID {}", toEdit.getId());
        noteService.update(toEdit);
        return "redirect:/admin/notes";
    }
}
