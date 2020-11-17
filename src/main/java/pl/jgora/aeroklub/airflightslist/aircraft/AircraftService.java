package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AircraftService {
    private final AircraftRepository aircraftRepository;

    List<Aircraft> findAll() {
        return aircraftRepository.findAircraftsByOrderByType();
    }

    public Aircraft findById(Long id) {
        return aircraftRepository.findFirstById(id);
    }

    void activationUpdate(Aircraft aircraft, boolean active) {
        aircraft.setActive(active);

        aircraftRepository.save(aircraft);
    }

    void update(Aircraft aircraft) {
        if (aircraft != null && aircraft.getId() != null) {
            Aircraft toEdit = findById(aircraft.getId());
            if (toEdit != null) {
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
    }

    List<Aircraft> filteredAircrafts(AircraftFilter aircraftFilter) {
        return aircraftRepository.filteringAircrafts(aircraftFilter);
    }

    public Set<Aircraft> getEngineAircrafts() {
        return aircraftRepository.findByEngineTrueOrderByType();
    }

    public Set<Aircraft> getGliders() {
        return aircraftRepository.findByEngineFalseOrderByType();
    }
}
