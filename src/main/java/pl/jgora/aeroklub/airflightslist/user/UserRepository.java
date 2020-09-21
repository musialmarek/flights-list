package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String name);

    Set<User> findByOrderByUserName();

    User findFirstById(Long id);
}
