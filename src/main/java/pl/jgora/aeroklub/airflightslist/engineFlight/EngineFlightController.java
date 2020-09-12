package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/engine-flights")
public class EngineFlightController {
    private final EngineFlightService engineFlightService;

    @GetMapping
    public String engineFlightsDates(Model model, @RequestParam(name = "year",required = false) Integer year){
        Set<LocalDate> allFlyingDays;
        if(year==null){
            year = LocalDate.now().getYear();
        }
        allFlyingDays = engineFlightService.getAllFlyingDays(year);
        model.addAttribute("dates",allFlyingDays);
        return "flights/engine-dates";
    }
}
