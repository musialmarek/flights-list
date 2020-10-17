package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @GetMapping("/send-email")
    @ResponseBody
    public String send() {
        Context context = new Context();
        context.setVariable("password","abcde1234!@#$");
        context.setVariable("userName","user@user.pl");
        String body = templateEngine.process("email/first-login-details", context);
        emailService.sendEmail("musialmarek.it@gmail.com", "Test Email Title", body);
        return "sent Email";
    }
}
