package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
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
            toEdit.setFia(pilot.getFia());
            toEdit.setFis(pilot.getFis());
            toEdit.setSepl(pilot.getSepl());
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

    List<Pilot> filteredPilots(String name,
                               String licence,
                               Boolean active,
                               Boolean gliderPilot,
                               Boolean gliderInstructor,
                               Boolean enginePilot,
                               Boolean engineInstructor,
                               Boolean tow) {
        StringBuilder whereSectionBuilder = new StringBuilder();
        Map<String,String> filters = new HashMap<>();
        if (name != null && !name.isEmpty()) {
            whereSectionBuilder.append(" p.name like concat('%',:name,'%') AND");
            filters.put("name",name);
        }
        if (licence != null && !licence.isEmpty()) {
            whereSectionBuilder.append(" p.licence like concat('%',:licence,'%') AND");
            filters.put("licence",licence);
        }
        if (active != null) {
            whereSectionBuilder.append(" p.active=").append(active).append(" AND");
        }
        if (gliderPilot != null) {
            whereSectionBuilder.append(" p.gliderPilot=").append(gliderPilot).append(" AND");
        }
        if (gliderInstructor != null) {
            whereSectionBuilder.append(" p.gliderInstructor=").append(gliderInstructor).append(" AND");
        }
        if (enginePilot != null) {
            whereSectionBuilder.append(" p.enginePilot=").append(enginePilot).append(" AND");
        }
        if (engineInstructor != null) {
            whereSectionBuilder.append(" p.engineInstructor=").append(engineInstructor).append(" AND");
        }
        if (tow != null) {
            whereSectionBuilder.append(" p.tow=").append(tow).append(" AND");
        }
        whereSectionBuilder.append(" p.id IS NOT NULL ");
        String whereSection = whereSectionBuilder.toString();
        log.info("\nWHERE SECTION \"{}\"", whereSection);
        return pilotRepository.filteringPilots(whereSection,filters);
    }
}
