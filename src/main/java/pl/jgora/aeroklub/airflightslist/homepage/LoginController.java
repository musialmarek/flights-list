package pl.jgora.aeroklub.airflightslist.homepage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, String error, String logout){
        if (error != null)
            model.addAttribute("error", "Błędne hasło lub nazwa użytkownika");

        if (logout != null)
            model.addAttribute("message", "Nastąpiło poprawne wylogwanie");

        return "homepage/login";
    }
}
