package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jgora.aeroklub.airflightslist.model.User;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;
import pl.jgora.aeroklub.airflightslist.role.RoleRepository;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AddEditUserController {
    private final PilotService pilotService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/admin/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pilots", pilotService.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "users/add-user";
    }

    @PostMapping("admin/user/add")
    public String addUserAction(User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/deactivate")
    public String deactivate(User user) {
        User toDeactivate = userService.findById(user.getId());
        log.debug("\nUSER TO DEACTIVATE : {}", toDeactivate.getUserName());
        toDeactivate.setActive(false);
        userService.activationUpdate(toDeactivate);
        log.debug("\nUSER: {} IS NOT ACTIVE NOW", toDeactivate.getUserName());
        return "redirect:/admin/users";
    }

    @PostMapping("/activate")
    public String activate(User user) {
        User toActivate = userService.findById(user.getId());
        log.debug("\nUSER TO ACTIVATE : {}", toActivate.getUserName());
        toActivate.setActive(true);
        userService.activationUpdate(toActivate);
        log.debug("\nUSER: {} IS ACTIVE NOW", toActivate.getUserName());
        return "redirect:/admin/users";
    }
}
