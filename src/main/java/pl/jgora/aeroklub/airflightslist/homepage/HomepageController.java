package pl.jgora.aeroklub.airflightslist.homepage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jgora.aeroklub.airflightslist.model.User;
import pl.jgora.aeroklub.airflightslist.user.CurrentUser;
import pl.jgora.aeroklub.airflightslist.user.UserService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomepageController {
    private final UserService userService;
    @GetMapping("/")
    public String home(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        if (currentUser != null) {
            User user = userService.findById(currentUser.getUser().getId());
            model.addAttribute("user", user);
            model.addAttribute("pilot", user.getPilot());
            log.debug("\n ADDED USER AND USER'S PILOT TO MODEL");
        }
        log.debug("\nEntered homepage");
        return "homepage/index";
    }
}
