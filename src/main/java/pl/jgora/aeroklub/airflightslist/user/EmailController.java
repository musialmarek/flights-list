package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @GetMapping("/send-email")
    @ResponseBody
    public String send() {
        emailService.sendEmail("musialmarek.it@gmail.com", "Test Email Title", "Test Email");
        return "sent Email";
    }
}
