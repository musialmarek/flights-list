package pl.jgora.aeroklub.airflightslist.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.account.AccountRepository;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.gliderFlight.GliderFlightService;
import pl.jgora.aeroklub.airflightslist.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final AccountRepository accountRepository;
    private final NoteRepository noteRepository;
    private final EngineFlightService engineFlightService;
    private final GliderFlightService gliderFlightService;

    public void createNote(List<GliderFlight> flights, List<EngineFlight> towingFlights) {
        Note note = getNewNoteToChargeFlights(AbstractFlightService.gliderToAbstractFlightList(flights), false);
        BigDecimal value = new BigDecimal(0);
        value = addFlightsCostToNoteValue(AbstractFlightService.gliderToAbstractFlightList(flights), value);
        value = addFlightsCostToNoteValue(AbstractFlightService.engineToAbstractFlightList(towingFlights), value);
        note.setValue(value);
        note.setCategory(NoteCategory.LOTY);
        Note savedNote = noteRepository.save(note);
        setNoteInGliderFlights(flights, savedNote);
        setNoteInEngineFlights(towingFlights, savedNote);
    }

    public void createNote(List<EngineFlight> flights) {
        Note note = getNewNoteToChargeFlights(AbstractFlightService.engineToAbstractFlightList(flights), true);
        BigDecimal value = new BigDecimal(0);
        value = addFlightsCostToNoteValue(AbstractFlightService.engineToAbstractFlightList(flights), value);
        note.setValue(value);
        note.setCategory(NoteCategory.LOTY);
        Note savedNote = noteRepository.save(note);
        setNoteInEngineFlights(flights, savedNote);
    }

    public List<Note> getFilteredNotes(FlightsFilter flightsFilter) {
        flightsFilter.setNote(true);
        return noteRepository.getFilteredNotes(flightsFilter);
    }

    private Note getNewNoteToChargeFlights(List<AbstractFlight> gliderFlights, boolean engine) {
        Note note = new Note();
        note.setPayer(gliderFlights.get(0).getPayer());
        note.setAccount(accountRepository.findFirstByName("MAIN"));
        note.setPaid(false);
        note.setActive(false);
        note.setNumber(generateNumber());
        if (engine) {
            note.setDescription("SKŁADKA SEKCYJNA SAMOLOTOWA");
        } else {
            note.setDescription("SKŁADKA SEKCYJNA SZYBOWCOWA");
        }
        note.setDate(LocalDate.now());
        return note;
    }

    private BigDecimal addFlightsCostToNoteValue(List<AbstractFlight> flights, BigDecimal value) {
        for (AbstractFlight flight : flights) {
            BigDecimal cost = flight.getCost();
            if (cost != null) {
                value = value.add(cost);
            }
        }
        return value;
    }

    private void setNoteInGliderFlights(List<GliderFlight> flights, Note savedNote) {
        for (GliderFlight flight : flights) {
            gliderFlightService.setNote(savedNote, flight);
        }
    }

    private void setNoteInEngineFlights(List<EngineFlight> flights, Note savedNote) {
        for (EngineFlight flight : flights) {
            engineFlightService.setNote(savedNote, flight);
        }
    }

    private String generateNumber() {
        StringBuilder sb = new StringBuilder();
        String nextNoteNumber = getNextNoteNumber();
        switch (nextNoteNumber.length()) {
            case 1:
                sb.append("00");
                break;
            case 2:
                sb.append("0");
                break;
        }
        sb.append(nextNoteNumber).append("/").append(LocalDate.now().getYear());
        return sb.toString();
    }

    private String getNextNoteNumber() {
        int currentYear = LocalDate.now().getYear();
        return Integer.toString(noteRepository.countByDateBetween
                (LocalDate.of(currentYear, 1, 1),
                        LocalDate.of(currentYear, 12, 31)) + 1);
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note findById(Long id) {
        return noteRepository.findFirstById(id);
    }

    public void activationUpdate(Note toActivateDeactivate, boolean active) {
        toActivateDeactivate.setActive(active);
        noteRepository.save(toActivateDeactivate);
    }

    public Note save(Note note) {
        note.setNumber(generateNumber());
        return noteRepository.save(note);
    }

    public Note update(Note note) {
        Note toEdit = new Note();
        if (note != null && note.getId() != null) {
            toEdit = noteRepository.findFirstById(note.getId());
            if (toEdit != null) {
                toEdit.setActive(note.getActive());
                toEdit.setAnnotation(note.getAnnotation());
                toEdit.setCategory(note.getCategory());
                toEdit.setDate(note.getDate());
                toEdit.setDateLimit(note.getDateLimit());
                toEdit.setDescription(note.getDescription());
                toEdit.setNumber(note.getNumber());
                toEdit.setPaidValue(note.getPaidValue());
                toEdit.setPayer(note.getPayer());
                toEdit.setPayerData(note.getPayerData());
                toEdit.setValue(note.getValue());
                toEdit.setAccount(note.getAccount());
                noteRepository.save(toEdit);
            }
        }
        return toEdit;
    }
}
