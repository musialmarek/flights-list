package pl.jgora.aeroklub.airflightslist.note;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Note;

import java.time.LocalDate;

public interface NoteRepository extends JpaRepository<Note, Long> {

    int countByDateBetween(LocalDate yearBegin, LocalDate yearEnd);
}
