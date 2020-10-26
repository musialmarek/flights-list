package pl.jgora.aeroklub.airflightslist.user;

import pl.jgora.aeroklub.airflightslist.model.User;

public interface EmailService {

    void sendEmail(String to, String subject, String content, boolean html);

    String getRegistrationMailContent(User user);

    String getPasswordRecoveryConfirmingMailContent(User user);

    String getPasswordRecoveryMailContent(User user);

    String getConfirmingNewEmailContent(Long id, String username, String token);
}
