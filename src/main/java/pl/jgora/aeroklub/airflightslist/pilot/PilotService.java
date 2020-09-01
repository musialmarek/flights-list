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
        return pilotRepository.findFirstById(id);
    }

    void activationUpdate(Pilot pilot) {
        pilotRepository.save(pilot);
    }

    void update(Pilot pilot) {
        if (pilot != null && pilot.getId() != null) {
            Pilot toEdit = findById(pilot.getId());
            toEdit.setFiA(pilot.getFiA());
            toEdit.setFiS(pilot.getFiS());
            toEdit.setSEPL(pilot.getSEPL());
            toEdit.setActive(pilot.getActive());
            toEdit.setEngineInstructor(pilot.getEngineInstructor());
            toEdit.setEnginePilot(pilot.getEnginePilot());
            toEdit.setEnginePractise(pilot.getEnginePractise());
            toEdit.setGliderInstructor(pilot.getGliderInstructor());
            toEdit.setGliderPilot(pilot.getGliderPilot());
            toEdit.setGliderPractise(pilot.getGliderPractise());
            toEdit.setLicence(pilot.getLicence());
            toEdit.setMedicine(pilot.getMedicine());
            toEdit.setName(pilot.getName());
            toEdit.setTheory(pilot.getTheory());
            toEdit.setTow(pilot.getTow());
            pilotRepository.save(toEdit);
        }
    }
}
