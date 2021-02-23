package pl.jgora.aeroklub.airflightslist.note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.account.AccountRepository;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.gliderFlight.GliderFlightService;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Note;

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
        Note note = getNewNoteToChargeFlights(AbstractFlightService.gliderToAbstractFlightList(flights));
        BigDecimal value = new BigDecimal(0);
        value = addFlightsCostToNoteValue(AbstractFlightService.gliderToAbstractFlightList(flights), value);
        value = addFlightsCostToNoteValue(AbstractFlightService.engineToAbstractFlightList(towingFlights), value);
        note.setValue(value);
        Note savedNote = noteRepository.save(note);
        setNoteInGliderFlights(flights, savedNote);
        setNoteInEngineFlights(towingFlights, savedNote);
    }

    public void createNote(List<EngineFlight> flights) {
        Note note = getNewNoteToChargeFlights(AbstractFlightService.engineToAbstractFlightList(flights));
        BigDecimal value = new BigDecimal(0);
        value = addFlightsCostToNoteValue(AbstractFlightService.engineToAbstractFlightList(flights), value);
        note.setValue(value);
        Note savedNote = noteRepository.save(note);
        setNoteInEngineFlights(flights, savedNote);
    }

    Note getNewNoteToChargeFlights(List<AbstractFlight> gliderFlights) {
        Note note = new Note();
        note.setPayer(gliderFlights.get(0).getPayer());
        note.setAccount(accountRepository.findFirstByName("MAIN"));
        note.setPaid(false);
        note.setActive(false);
        note.setNumber(generateNumber());
        note.setDescription("SKŁADKA SEKCYJNA");
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
        sb.append(getNextNoteNumber()).append("/").append(LocalDate.now().getYear());
        return sb.toString();
    }

    private String getNextNoteNumber() {
        int currentYear = LocalDate.now().getYear();
        return Integer.toString(noteRepository.countByDateBetween
                (LocalDate.of(currentYear, 1, 1),
                        LocalDate.of(currentYear, 12, 31)) + 1);
    }
}
