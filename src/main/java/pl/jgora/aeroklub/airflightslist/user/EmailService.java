package pl.jgora.aeroklub.airflightslist.user;

public interface EmailService {
    void sendEmail(String to,String subject,String content);
}
