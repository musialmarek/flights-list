package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

@Service
@Slf4j


public class AbstractFlightService {
    public static void updateFlight(AbstractFlight flight, AbstractFlight toEdit) {
        toEdit.setActive(flight.getActive());
        toEdit.setAircraftRegistrationNumber(flight.getAircraftRegistrationNumber());
        toEdit.setCopilotName(flight.getCopilotName());
        toEdit.setDate(flight.getDate());
        toEdit.setInstructor(flight.getInstructor());
        toEdit.setPicName(flight.getPicName());
        toEdit.setStart(flight.getStart());
        toEdit.setTask(flight.getTask());
        toEdit.setTouchdown(flight.getTouchdown());
        toEdit.setAircraft(flight.getAircraft());
        toEdit.setCopilot(flight.getCopilot());
        toEdit.setPic(flight.getPic());
        toEdit.setAircraftType(flight.getAircraftType());
    }

    public static void replacePilots(AbstractFlight flight) {
        if (flight.getInstructor()) {
            log.debug("\n REPLACING PILOTS - FLIGHT IS INSTRUCTOR FLIGHT ");
            Pilot enteredCopilot = flight.getCopilot();
            String enteredCopilotName = flight.getCopilotName();
            String enteredPicName = flight.getPicName();
            Pilot enteredPic = flight.getPic();
            Pilot replacedPic = null;
            Pilot replacedCopilot = null;
            String replacedPicName = null;
            String replacedCopilotName = null;
            if (enteredCopilot != null) {
                replacedPic = enteredCopilot;
            } else if (enteredCopilotName != null && !enteredCopilotName.isEmpty()) {
                replacedPicName = enteredCopilotName;
            } else {
                throw new IllegalArgumentException("There is no instructor");

            }

            if (enteredPic != null) {
                replacedCopilot = enteredPic;
            } else if (enteredPicName != null && !enteredPicName.isEmpty()) {
                replacedCopilotName = enteredPicName;
            } else {
                throw new IllegalArgumentException("There is no student");
            }
            flight.setPic(replacedPic);
            flight.setCopilot(replacedCopilot);
            flight.setPicName(replacedPicName);
            flight.setCopilotName(replacedCopilotName);
        }
    }
}
