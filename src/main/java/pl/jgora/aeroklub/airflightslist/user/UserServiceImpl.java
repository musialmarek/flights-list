package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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
    private final TemplateEngine templateEngine;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void saveUser(User user) {
        emailService.sendEmail(user.getUserName(), "PIERWSZE LOGOWANIE W E-CHRONOMETRAŻU AJ", registrationMailContent(user));
        log.debug("ENCODING PASSWORD");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        if (user.getRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRole(userRole);
        }
        userRepository.save(user);
    }

    private String registrationMailContent(User user) {
        String password = randomPassword();
        Context context = new Context();
        context.setVariable("userName", user.getUserName());
        context.setVariable("password", password);
        user.setPassword(password);
        return templateEngine.process("email/first-login-details", context);

    }

    private String randomPassword() {
        return RandomString.make(10);
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
}
