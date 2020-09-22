package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.Role;
import pl.jgora.aeroklub.airflightslist.model.User;
import pl.jgora.aeroklub.airflightslist.role.RoleRepository;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AddEditUserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @ModelAttribute("pilots")
    public Set<Pilot> getFreePilots() {

        return userService.getAvailablePilots();
    }

    @ModelAttribute("roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/admin/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/add-user";
    }

    @PostMapping("admin/user/add")
    public String addUserAction(User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/edit")
    public String editUserForm(Model model, @RequestParam Long id) {
        model.addAttribute("user", userService.findById(id));
        return "users/edit-user";
    }

    @PostMapping("admin/user/edit")
    public String editUserAction(User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
}
