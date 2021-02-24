package pl.jgora.aeroklub.airflightslist.account;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jgora.aeroklub.airflightslist.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findFirstByName(String name);
}
