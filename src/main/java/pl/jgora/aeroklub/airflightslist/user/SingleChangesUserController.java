package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.User;

@Controller
@RequiredArgsConstructor
@Slf4j

public class SingleChangesUserController {
    private final UserService userService;
    private final EmailService emailService;

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
        if (user.getPassword().equals(user.getConfirmingPassword())) {
            User toEdit = userService.findById(currentUser.getUser().getId());
            toEdit.setPassword(user.getPassword());
            userService.updateUser(toEdit);
            return "redirect:/user/details?password=changed";
        }
        return "redirect:/user/password?confirming=false";
    }

    @GetMapping("/user/email")
    public String changeEmailForm(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("ADDING USER AND USER'S PILOT TO MODEL");
        model.addAttribute("user", userService.findById(currentUser.getUser().getId()));
        return "users/new-email";
    }

    @PostMapping("user/email/confirm")
    public String changeEmailConfirming(User user, @AuthenticationPrincipal CurrentUser currentUser) {
        log.debug("checking available of new email");
        if (userService.isEmailAvailable(user.getUserName())) {
            User toEdit = currentUser.getUser();
            toEdit.setUserName(user.getUserName());
            log.debug("sending email to confirm new address");
            userService.confirmChangingEmail(toEdit);
            return "redirect:/user/details?email=confirm";
        }
        return "redirect:/user/email?email-available=false";
    }

    @GetMapping("/user/email/change")
    public String changeEmailAction(@RequestParam Long id, @RequestParam String userName, @RequestParam String token) {
        log.debug("GETTING USER FROM DATABASE ");
        User user = userService.findById(id);
        if (user != null && token.equals(user.getToken()) && userService.isEmailAvailable(userName)) {
            log.debug("changing userName from {} to {}", user.getUserName(), userName);
            user.setUserName(userName);
            userService.updateUser(user);
            return "redirect:/user/details?email=changed";
        }
        return "redirect:/user/email?email-available=false";
    }
}
