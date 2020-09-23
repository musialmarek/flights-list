package pl.jgora.aeroklub.airflightslist.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftRepository;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightRepository;
import pl.jgora.aeroklub.airflightslist.engineFlight.EngineFlightService;
import pl.jgora.aeroklub.airflightslist.gliderFlight.GliderFlightRepository;
import pl.jgora.aeroklub.airflightslist.gliderFlight.GliderFlightService;
import pl.jgora.aeroklub.airflightslist.pilot.PilotRepository;
import pl.jgora.aeroklub.airflightslist.user.UserService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final UserService userService;
    private final AircraftRepository aircraftRepository;
    private final PilotRepository pilotRepository;
    private final GliderFlightRepository gliderFlightRepository;
    private final EngineFlightRepository engineFlightRepository;
    private final GliderFlightService gliderFlightService;
    private final EngineFlightService engineFlightService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
