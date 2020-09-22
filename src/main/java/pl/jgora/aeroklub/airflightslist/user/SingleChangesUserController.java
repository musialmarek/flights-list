package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.User;

@Controller
@RequiredArgsConstructor
@Slf4j

public class SingleChangesUserController {
    private final UserService userService;

    @ModelAttribute("pilot")
    Pilot getPilot(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.findById(currentUser.getUser().getId()).getPilot();
    }

    @PostMapping("admin/user/deactivate")
    public String deactivate(User user) {
        User toDeactivate = userService.findById(user.getId());
        log.debug("\nUSER TO DEACTIVATE : {}", toDeactivate.getUserName());
        toDeactivate.setActive(false);
        userService.activationUpdate(toDeactivate);
        log.debug("\nUSER: {} IS NOT ACTIVE NOW", toDeactivate.getUserName());
        return "redirect:/admin/users";
    }

    @PostMapping("admin/user/activate")
    public String activate(User user) {
        User toActivate = userService.findById(user.getId());
        log.debug("\nUSER TO ACTIVATE : {}", toActivate.getUserName());
        toActivate.setActive(true);
        userService.activationUpdate(toActivate);
        log.debug("\nUSER: {} IS ACTIVE NOW", toActivate.getUserName());
        return "redirect:/admin/users";
    }

    @GetMapping("/user/password")
    public String changePasswordForm(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("ADDING USER AND USER'S PILOT TO MODEL");
        model.addAttribute("user", userService.findById(currentUser.getUser().getId()));
        return "users/new-password";
    }

    @PostMapping("/user/password")
    public String changePasswordAction(User user, @AuthenticationPrincipal CurrentUser currentUser) {
        User toEdit = userService.findById(currentUser.getUser().getId());
        toEdit.setPassword(user.getPassword());
        userService.updateUser(toEdit);
        return "redirect:/user/details";
    }

    @GetMapping("/user/email")
    public String changeEmailForm(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("ADDING USER AND USER'S PILOT TO MODEL");
        model.addAttribute("user", userService.findById(currentUser.getUser().getId()));
        return "users/new-email";
    }

    @PostMapping("/user/email")
    public String changeEmailAction(User user, @AuthenticationPrincipal CurrentUser currentUser) {
        User toEdit = userService.findById(currentUser.getUser().getId());
        toEdit.setEmail(user.getEmail());
        userService.updateUser(toEdit);
        return "redirect:/user/details";
    }
}
