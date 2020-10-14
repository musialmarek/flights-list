package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void saveUser(User user) {
        log.debug("\n ENCODING PASSWORD");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        if (user.getRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRole(userRole);
        }
        userRepository.save(user);
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
        toEdit.setPassword(user.getPassword());
        toEdit.setActive(user.getActive());
        toEdit.setPilot(user.getPilot());
        toEdit.setEmail(user.getEmail());
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

    @Override
    public User findByUserNameOrEmail(String userNameOrEmail) {
        return userRepository.findFirstByUserNameOrEmail(userNameOrEmail, userNameOrEmail);
    }
}
