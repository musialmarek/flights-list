package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        MimeMessage mail = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
//            helper.setReplyTo("aeroklubjeleniogorski@gmail.com");
//            helper.setFrom("aeroklubjeleniogorski@gmail.com");
            helper.setSubject(subject);
            helper.setText(content);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(mail);
    }
}
