package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import java.util.Map;

@Service
@Slf4j


public class AbstractFlightService {
    public static void updateFlight(AbstractFlight flight, AbstractFlight toEdit) {
        toEdit.setActive(flight.getActive());
        toEdit.setAircraftRegistrationNumber(flight.getAircraftRegistrationNumber());
        toEdit.setCopilotName(flight.getCopilotName());
        toEdit.setDate(flight.getDate());
        toEdit.setFlightTime(flight.getFlightTime());
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
            Pilot pic = flight.getCopilot();
            Pilot copilot = flight.getPic();
            flight.setPic(pic);
            flight.setCopilot(copilot);
        }
    }
}
