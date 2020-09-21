package pl.jgora.aeroklub.airflightslist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Role;
import pl.jgora.aeroklub.airflightslist.model.User;
import pl.jgora.aeroklub.airflightslist.role.RoleRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRole(userRole);
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
        userRepository.save(user);
    }
}
