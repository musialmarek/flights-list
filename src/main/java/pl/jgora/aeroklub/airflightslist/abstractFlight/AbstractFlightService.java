package pl.jgora.aeroklub.airflightslist.abstractFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.*;
import pl.jgora.aeroklub.airflightslist.price.PriceService;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class AbstractFlightService {
    private final PriceService priceService;

    public void updateFlight(AbstractFlight flight, AbstractFlight toEdit) {
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
        toEdit.setCharge(flight.getCharge());
        toEdit.setPayer(flight.getPayer());
        toEdit.setNote(flight.getNote());
        if (flight.getCharge()) {
            if (flight.getCost() == null || flight.getCost().equals(toEdit.getCost())) {
                toEdit.setCost(calculateCost(flight));
            } else {
                toEdit.setCost(flight.getCost());
            }
        }
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

    public static List<AbstractFlight> engineToAbstractFlightList(List<EngineFlight> flights) {
        return flights.stream().map(flight -> (AbstractFlight) flight).collect(Collectors.toList());
    }

    public static List<AbstractFlight> gliderToAbstractFlightList(List<GliderFlight> flights) {
        return flights.stream().map(flight -> (AbstractFlight) flight).collect(Collectors.toList());
    }

    public BigDecimal calculateCost(AbstractFlight flight) {
        Price prices = new Price(0);
        flight.setFlightTime((int) (Duration.between(flight.getStart(), flight.getTouchdown()).getSeconds() / 60));
        if (flight.getAircraft() != null) {
            prices = flight.getAircraft().getPrice();
        }
        if ("HOL".equals(flight.getTask())) {
            prices = priceService.getTowingPrice();
        }
        BigDecimal price = prices.getOthers();
        if (flight.getPayer() != null && flight.getPayer().getNativeMember()) {
            price = prices.getNativeMember();
        }
        return price.multiply(new BigDecimal(flight.getFlightTime()));
    }
}
