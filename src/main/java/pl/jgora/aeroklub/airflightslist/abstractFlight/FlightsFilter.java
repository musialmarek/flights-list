package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.NoteCategory;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class FlightsFilter {
    private Aircraft aircraft;
    private Pilot pilot;
    private Boolean pic;
    private String type;
    private String registration;
    private Boolean active;
    private String from;
    private String to;
    private String task;
    private Boolean instructor;
    private Boolean tow;
    private String start;
    private Boolean note;
    private Pilot payer;
    private String payerName;
    private Boolean paid;
    private String noteNumber;
    private String category;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String whereSection;
    private Map<String, Object> filters = new HashMap<>();

    public String getWhereSection() {
        invoke();
        log.debug("WHERE SECTION: {} ", whereSection);
        return whereSection;
    }

    public Map<String, Object> getFilters() {
        invoke();
        log.debug("FILTERS VALUES {}", filters);
        return filters;
    }

    private void invoke() {
        StringBuilder whereSectionBuilder = new StringBuilder();

        if (pilot != null) {
            log.debug("FINDING BY PILOT");
            getWhereSectionFilteringFlightsByPilot(whereSectionBuilder);
        } else if (aircraft != null) {
            log.debug("FINDING BY AIRCRAFT");
            getWhereSectionFilteringFlightsByAircraft(whereSectionBuilder);
        }

        if (active != null) {
            log.debug("ACTIVE: {}", active);
            whereSectionBuilder.append(" f.active=").append(active).append(" AND");
        }
        if (note != null && note) {
            log.debug("FINDING NOTES");
            getWhereSectionFilteringNotes(whereSectionBuilder);
        }

        if (from != null && !from.isEmpty()) {
            log.debug("FROM: {}", from);
            try {
                LocalDate fromDate = LocalDate.parse(from);
                whereSectionBuilder.append(" f.date >= :from AND");
                filters.put("from", fromDate);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                log.debug("WRONG DATA FORMAT IN \"FROM\" PARAMETER");
            }
        }

        if (to != null && !to.isEmpty()) {
            log.debug("TO: {}", to);
            try {
                LocalDate toDate = LocalDate.parse(to);
                whereSectionBuilder.append(" f.date <= :to AND");
                filters.put("to", toDate);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                log.debug("WRONG DATA FORMAT IN \"TO\" PARAMETER");
            }
        }

        if (task != null && !task.isEmpty()) {
            log.debug("TASK: {}", task);
            whereSectionBuilder.append(" f.task like concat('%',:task,'%') AND");
            filters.put("task", task);
        }

        if (instructor != null) {
            log.debug("INSTRUCTOR: {}", instructor);
            whereSectionBuilder.append(" f.instructor=").append(instructor).append(" AND");
            whereSection = whereSectionBuilder.toString();
        }

        if (start != null && !start.isEmpty()) {
            log.debug("START METHOD: {}", start);
            StartMethod startMethod = StartMethod.valueOf(start);
            whereSectionBuilder.append(" f.startMethod = :start AND");
            filters.put("start", startMethod);
        }
        whereSectionBuilder.append(" f.id IS NOT NULL ");
        whereSection = whereSectionBuilder.toString();
    }

    private void getWhereSectionFilteringFlightsByPilot(StringBuilder whereSectionBuilder) {
        {
            if (pic != null) {
                log.debug("PIC: {}", pic);
                if (pic) {
                    whereSectionBuilder.append(" (f.pic = :pilot OR f.picName like concat('%',:name,'%')) AND");
                } else {
                    whereSectionBuilder.append(" (f.copilot = :pilot OR f.copilotName like concat('%',:name,'%')) AND");
                }

            } else {
                log.debug("PIC IS NULL");
                whereSectionBuilder.append(" (f.pic = :pilot OR f.picName = :name OR f.copilot = :pilot OR f.copilotName = :name) AND");
            }
            filters.put("pilot", pilot);
            filters.put("name", pilot.getName());
            if (aircraft != null) {
                log.debug("AIRCRAFT : {}", aircraft);
                whereSectionBuilder.append(" (f.aircraft = :aircraft OR (f.aircraft.type = :type AND f.aircraft.registrationNumber = :registration)) AND");
                filters.put("aircraft", aircraft);
                filters.put("type", aircraft.getType());
                filters.put("registration", aircraft.getRegistrationNumber());
            } else {
                if (type != null && !type.isEmpty()) {
                    log.debug("TYPE : {}", type);
                    whereSectionBuilder.append(" f.aircraft.type like concat('%',:type,'%') AND");
                    filters.put("type", type);
                }
                if (registration != null && !registration.isEmpty()) {
                    log.debug("REGISTRATION {}", registration);
                    whereSectionBuilder.append(" f.aircraft.registrationNumber like concat('%',:registration,'%') AND");
                    filters.put("registration", registration);
                }
            }
        }
    }

    private void getWhereSectionFilteringFlightsByAircraft(StringBuilder whereSectionBuilder) {
        whereSectionBuilder.append(" (f.aircraft = :aircraft OR (f.aircraft.type = :type AND f.aircraft.registrationNumber = :registration)) AND");
        filters.put("aircraft", aircraft);
        filters.put("type", aircraft.getType());
        filters.put("registration", aircraft.getRegistrationNumber());
        if (tow != null) {
            log.debug("TOW: {}", tow);
            whereSectionBuilder.append(" f.tow=").append(tow).append(" AND");
        }
    }

    private void getWhereSectionFilteringNotes(StringBuilder whereSectionBuilder) {
        if (payer != null && payer.getId() != null) {
            whereSectionBuilder.append(" f.payer = :payer OR f.payerData like concat('%',:payerData,'%') AND");
            filters.put("payer", payer);
            filters.put("payerData", payer.getName());
        }
        if (payerName != null && payer == null && !payerName.isEmpty()) {
            whereSectionBuilder.append(" f.payerData like concat('%',:payerData,'%') AND");
            filters.put("payerData", payerName);
        }
        if (noteNumber != null && !noteNumber.isEmpty()) {
            whereSectionBuilder.append(" f.number like concat('%',:number,'%') AND");
            filters.put("number", noteNumber);
        }
        if (paid != null) {
            whereSectionBuilder.append(" f.paid=").append(paid).append(" AND");
        }
        if (minValue != null) {
            whereSectionBuilder.append(" ").append(minValue).append(" <= f.value AND");
        }
        if (maxValue != null) {
            whereSectionBuilder.append(" f.value <= ").append(minValue).append(" AND");
        }
        if (category != null) {
            NoteCategory noteCategory = NoteCategory.valueOf(category);
            whereSectionBuilder.append(" f.category = :category AND");
            filters.put("category", noteCategory);
        }
    }
}
