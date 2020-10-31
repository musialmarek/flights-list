package pl.jgora.aeroklub.airflightslist.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/message")
public class MessageController {
    @GetMapping
    public String message(@RequestParam String message, Model model) {
        switch (message) {
            case ("recovery-password-confirm"):
                model.addAttribute("message", "Na podany email został wysłany link potwierdzający chęć odzyskania hasła");
                break;
            case ("recovery-password"):
                model.addAttribute("message", "Na podany mail zostało wysłane nowe hasło");
                break;
            case ("incorrect-token"):
                model.addAttribute("message", "Wystąpił błąd, token może być nieaktywny, " +
                        "spróbuj powtórzyć procedurę, lub skontaktuj się z administratorem");
        }
        return "homepage/message";
    }
}
