package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    public static void getWhereSectionFilteringFlightsByAircraft(Aircraft aircraft, Boolean active, String from, String to, String task, Boolean instructor, Boolean tow, String start, StringBuilder whereSectionBuilder, Map<String, Object> filters) {
        whereSectionBuilder.append(" (f.aircraft = :aircraft OR (f.aircraft.type = :type AND f.aircraft.registrationNumber = :registration)) AND");
        filters.put("aircraft", aircraft);
        filters.put("type", aircraft.getType());
        filters.put("registration", aircraft.getRegistrationNumber());
        getWhereSectionFilteringFlights(active, from, to, task, instructor, whereSectionBuilder, filters);
        if (tow != null) {
            log.debug("TOW: {}", tow);
            whereSectionBuilder.append(" f.tow=").append(tow).append(" AND");
        }
        if (start != null && !start.isEmpty()) {
            log.debug("START METHOD: {}", start);
            StartMethod startMethod = StartMethod.valueOf(start);
            whereSectionBuilder.append(" f.startMethod = :start AND");
            filters.put("start", startMethod);
        }
        whereSectionBuilder.append(" f.id IS NOT NULL ");

    }

    public static void getWhereSectionFilteringFlights(Boolean active, String from, String to, String task, Boolean instructor, StringBuilder whereSectionBuilder, Map<String, Object> filters) {
        if (active != null) {
            log.debug("\nACTIVE: {}", active);
            whereSectionBuilder.append(" f.active=").append(active).append(" AND");
        }
        if (from != null && !from.isEmpty()) {
            log.debug("\nFROM: {}", from);
            try {
                LocalDate fromDate = LocalDate.parse(from);
                whereSectionBuilder.append(" f.date >= :from AND");
                filters.put("from", fromDate);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                log.debug("\nWRONG DATA FORMAT IN \"FROM\" PARAMETER");
            }
        }
        if (to != null && !to.isEmpty()) {
            log.debug("\nTO: {}", to);
            try {
                LocalDate toDate = LocalDate.parse(to);
                whereSectionBuilder.append(" f.date <= :to AND");
                filters.put("to", toDate);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                log.debug("\nWRONG DATA FORMAT IN \"TO\" PARAMETER");
            }
        }
        if (task != null && !task.isEmpty()) {
            log.debug("\nTASK: {}", task);
            whereSectionBuilder.append(" f.task like concat('%',:task,'%') AND");
            filters.put("task", task);
        }
        if (instructor != null) {
            log.debug("INSTRUCTOR: {}", instructor);
            whereSectionBuilder.append(" f.instructor=").append(instructor).append(" AND");
        }
    }

    public static void getWhereSectionFilteringFlightsByPilot(Pilot pilot, Boolean active, String from, String to, String task, Boolean pic, Boolean instructor, Aircraft aircraft, String type, String registration, StringBuilder whereSectionBuilder, Map<String, Object> filters) {
        getWhereSectionFilteringFlights(active, from, to, task, instructor, whereSectionBuilder, filters);
        if (pic != null) {
            log.debug("\nPIC: {}", pic);
            if (pic == true) {
                whereSectionBuilder.append(" (f.pic = :pilot OR f.picName like concat('%',:name,'%')) AND");
            } else {
                whereSectionBuilder.append(" (f.copilot = :pilot OR f.copilotName like concat('%',:name,'%')) AND");
            }

        } else {
            log.debug("\nPIC IS NULL");
            whereSectionBuilder.append(" (f.pic = :pilot OR f.picName = :name OR f.copilot = :pilot OR f.copilotName = :name) AND");
        }
        filters.put("pilot", pilot);
        filters.put("name", pilot.getName());
        if (aircraft != null) {
            log.debug("\nAIRCRAFT : {}", aircraft);
            whereSectionBuilder.append(" (f.aircraft = :aircraft OR (f.aircraft.type = :type AND f.aircraft.registrationNumber = :registration)) AND");
            filters.put("aircraft", aircraft);
            filters.put("type", aircraft.getType());
            filters.put("registration", aircraft.getRegistrationNumber());
        } else {
            if (type != null && !type.isEmpty()) {
                log.debug("\nTYPE : {}", type);
                whereSectionBuilder.append(" f.aircraft.type like concat('%',:type,'%') AND");
                filters.put("type", type);
            }
            if (registration != null && !registration.isEmpty()) {
                log.debug("\nREGISTRATION {}", registration);
                whereSectionBuilder.append(" f.aircraft.registrationNumber like concat('%',:registration,'%') AND");
                filters.put("registration", registration);
            }
        }
        whereSectionBuilder.append(" f.id IS NOT NULL ");
    }
}
