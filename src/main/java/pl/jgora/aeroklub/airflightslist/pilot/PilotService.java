package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PilotService {
    private final PilotRepository pilotRepository;

    List<Pilot> findAll() {
        return pilotRepository.findPilotsByOrderByName();
    }

    Pilot findById(Long id) {
        return pilotRepository.getOne(id);
    }

    void update(Pilot pilot){
        pilotRepository.save(pilot);
    }

}
