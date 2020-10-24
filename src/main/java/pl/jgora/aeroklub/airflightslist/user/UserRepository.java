package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String name);

    Set<User> findByOrderByUserName();

    User findFirstById(Long id);

    @Query("SELECT u.pilot FROM User u WHERE u.pilot IS NOT NULL ")
    Set<Pilot> findAllUnavailablePilots();

    @Query("SELECT u.userName FROM User u")
    Set<String> findAllUnavailableEmails();

    User findFirstByUserName(String userName);

    User findFirstByToken(String token);
}
