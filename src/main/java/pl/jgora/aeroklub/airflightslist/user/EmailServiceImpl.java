package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.jgora.aeroklub.airflightslist.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(String to, String subject, String content, boolean html) {
        MimeMessage mail = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(mail);
    }

    @Override
    public String getRegistrationMailContent(User user) {
        Context context = new Context();
        context.setVariable("userName", user.getUserName());
        context.setVariable("password", user.getPassword());
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/user/password";
        context.setVariable("url", url);
        return templateEngine.process("email/first-login-details", context);
    }

    @Override
    public String getPasswordRecoveryConfirmingMailContent(User user) {
        Context context = new Context();
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/recovery/" + user.getToken();
        context.setVariable("url", url);
        return templateEngine.process("email/password-recovery-confirming", context);
    }

    @Override
    public String getPasswordRecoveryMailContent(User user) {
        String password = RandomString.make(10);
        Context context = new Context();
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/user/password";
        context.setVariable("password", password);
        context.setVariable("url", url);
        user.setPassword(password);
        return templateEngine.process("email/password-recovery", context);
    }
}
