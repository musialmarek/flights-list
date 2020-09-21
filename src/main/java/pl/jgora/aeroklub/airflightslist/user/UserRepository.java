package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String name);

    Set<User> findByOrderByLogin();

    User findFirstById(Long id);
}
