package pl.jgora.aeroklub.airflightslist.role;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
