package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AircraftService {
    private final AircraftRepository aircraftRepository;

    List<Aircraft> findAll() {
        return aircraftRepository.findAircraftsByOrderByType();
    }

    Aircraft findById(Long id) {
        return aircraftRepository.findFirstById(id);
    }

    void activationUpdate(Aircraft aircraft) {
        aircraftRepository.save(aircraft);
    }

    void update(Aircraft aircraft) {
        if (aircraft != null && aircraft.getId() != null) {
            Aircraft toEdit = findById(aircraft.getId());
            toEdit.setActive(aircraft.getActive());
            toEdit.setEngine(aircraft.getEngine());
            toEdit.setType(aircraft.getType());
            toEdit.setRegistrationNumber(aircraft.getRegistrationNumber());
            toEdit.setFlyingTime(aircraft.getFlyingTime());
            toEdit.setArc(aircraft.getArc());
            toEdit.setInsurance(aircraft.getInsurance());
            toEdit.setNextWorkDate(aircraft.getNextWorkDate());
            toEdit.setNextWorkDateDescription(aircraft.getNextWorkDateDescription());
            toEdit.setNextWorkTime(aircraft.getNextWorkTime());
            toEdit.setNextWorkTimeDescription(aircraft.getNextWorkTimeDescription());
            toEdit.setFlyingTimeHours(aircraft.getFlyingTimeHours());
            toEdit.setFlyingTimeMinutes(aircraft.getFlyingTimeMinutes());
            toEdit.setWorkTimeHours(aircraft.getWorkTimeHours());
            toEdit.setWorkTimeMinutes(aircraft.getWorkTimeMinutes());
            aircraftRepository.save(toEdit);
        }
    }

    List<Aircraft> filteredAircrafts(String type, String registration, Boolean active, Boolean engine) {
        StringBuilder whereSectionBuilder = new StringBuilder();
        Map<String, String> filters = new HashMap<>();
        if (type != null && !type.isEmpty()) {
            whereSectionBuilder.append(" a.type like concat('%',:type,'%') AND");
            filters.put("type", type);
        }
        if (registration != null && !registration.isEmpty()) {
            whereSectionBuilder.append(" a.registrationNumber like concat('%',:registration,'%') AND");
            filters.put("registration", registration);
        }
        if (active != null) {
            whereSectionBuilder.append(" a.active=").append(active).append(" AND");
        }
        if (engine != null) {
            whereSectionBuilder.append(" a.engine=").append(engine).append(" AND");
        }
        whereSectionBuilder.append(" a.id IS NOT NULL ");
        String whereSection = whereSectionBuilder.toString();
        log.info("\nWHERE SECTION \"{}\"", whereSection);
        return aircraftRepository.filteringAircrafts(whereSection, filters);
    }
}
