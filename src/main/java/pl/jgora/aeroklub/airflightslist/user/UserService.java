package pl.jgora.aeroklub.airflightslist.user;

import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.Set;

public interface UserService {
    User findByUserName(String userName);

    void registerUser(User user);

    Set<User> findAll();

    User findById(Long id);

    void activationUpdate(User toDeactivate);

    void updateUser(User user);

    Set<Pilot> getAvailablePilots();

    boolean isEmailAvailable(String email);

    User findByToken(String token);

    void recoverPasswordConfirming(User user);

    void recoverPassword(User user);

    void confirmChangingEmail(User user);
}
