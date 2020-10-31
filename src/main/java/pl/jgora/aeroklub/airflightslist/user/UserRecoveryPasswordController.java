package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jgora.aeroklub.airflightslist.model.User;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recovery")
public class UserRecoveryPasswordController {
    private final UserService userService;

    @GetMapping("/new-password/{token}")
    public String recoveryPassword(@PathVariable String token, Model model) {
        log.debug("confirming recovery password");
        User user = userService.findByToken(token);
        if (user != null) {
            log.debug("sending new password");
            userService.recoverPassword(user);
            return "redirect:/message?message=recovery-password";
        }
        log.debug("can not found user with this token");
        model.addAttribute("message", "");
        return "redirect:/message?message=incorrect-token";
    }

    @PostMapping("/to-confirm")
    public String sendConfirmingEmail(String userName) {
        User user = userService.findByUserName(userName);
        if (user != null) {
            userService.recoverPasswordConfirming(user);
            return "redirect:/message?message=recovery-password-confirm";
        }
        return "redirect:/login?error";
    }
}