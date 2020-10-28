package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.Role;
import pl.jgora.aeroklub.airflightslist.model.User;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;
import pl.jgora.aeroklub.airflightslist.role.RoleRepository;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PilotService pilotService;
    private final EmailService emailService;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void registerUser(User user) {
        String password = RandomString.make(10);
        user.setPassword(password);
        emailService.sendEmail(user.getUserName(),
                "PIERWSZE LOGOWANIE W E-CHRONOMETRAŻU",
                emailService.getRegistrationMailContent(user),
                true);
        saveUser(user);
    }

    @Override
    public Set<User> findAll() {
        return userRepository.findByOrderByUserName();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findFirstById(id);
    }

    @Override
    public void activationUpdate(User user) {
        User toEdit = userRepository.findFirstById(user.getId());
        toEdit.setActive(user.getActive());
        userRepository.save(toEdit);
    }

    @Override
    public void updateUser(User user) {
        User toEdit = userRepository.findFirstById(user.getId());
        log.debug("\n CHANGING USER'S DATA");
        toEdit.setUserName(user.getUserName());
        toEdit.setPassword(user.getPassword());
        toEdit.setActive(user.getActive());
        toEdit.setPilot(user.getPilot());
        toEdit.setRole(user.getRole());
        toEdit.setToken(user.getToken());
        log.debug("\n SAVING EDITED USER");
        saveUser(toEdit);
    }

    @Override
    public Set<Pilot> getAvailablePilots() {
        return pilotService
                .findAll()
                .stream()
                .filter(pilot -> !userRepository.findAllUnavailablePilots().contains(pilot))
                .sorted(Comparator.comparing(Pilot::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.findAllUnavailableEmails().contains(email);
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findFirstByToken(token);
    }

    @Override
    public void recoverPasswordConfirming(User user) {
        String token = RandomString.make(10);
        user.setToken(token);
        emailService.sendEmail(user.getUserName(),
                "Odzyskiwanie hasła - potwierdzenie",
                emailService.getPasswordRecoveryConfirmingMailContent(user),
                true);
        log.debug("updating user with token");
        updateUser(user);
    }

    @Override
    public void recoverPassword(User user) {
        String password = RandomString.make(10);
        user.setPassword(password);
        emailService.sendEmail(user.getUserName(),
                "Odzyskiwanie hasła - nowe hasło",
                emailService.getPasswordRecoveryMailContent(user),
                true);
        user.setToken(null);
        log.debug("updating user with new password and reset token");
        updateUser(user);
    }

    @Override
    public void confirmChangingEmail(User user) {
        log.debug("creating confirming token");
        String token = RandomString.make(10);
        log.debug("setting token to user");
        User toEdit = userRepository.findFirstById(user.getId());
        toEdit.setToken(token);
        updateUser(toEdit);
        log.debug("sending email with confirming new email content");
        emailService.sendEmail(user.getUserName(),
                "Zmiana adresu email do logowania",
                emailService.getConfirmingNewEmailContent(user.getId(), user.getUserName(), token),
                true);
    }

    private void saveUser(User user) {
        log.debug("ENCODING PASSWORD");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        if (user.getRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRole(userRole);
        }
        userRepository.save(user);
    }
}
