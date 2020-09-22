package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jgora.aeroklub.airflightslist.model.User;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        log.debug("\nADDING LIST ALL USERS TO MODEL");
        model.addAttribute("users", userService.findAll());
        return "users/users";
    }

    @GetMapping("/user")
    public String userDashboard(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("\nADDING USER AND USER'S PILOT TO MODEL");
        User user = userService.findById(currentUser.getUser().getId());
        model.addAttribute("user", user);
        model.addAttribute("pilot", user.getPilot());
        return "users/user-dashboard";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("\nADDING USER (ADMIN)  TO MODEL");
        User user = userService.findById(currentUser.getUser().getId());
        model.addAttribute("user", user);
        model.addAttribute("pilot", user.getPilot());
        return "users/admin-dashboard";
    }

    @GetMapping("/user/details")
    public String userDetails(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("\nADDING USER AND USER'S PILOT TO MODEL");
        User user = userService.findById(currentUser.getUser().getId());
        model.addAttribute("user", user);
        model.addAttribute("pilot", user.getPilot());
        return "users/user-details";

    }

}

