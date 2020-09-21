package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User {
    private final pl.jgora.aeroklub.airflightslist.model.User user;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities, pl.jgora.aeroklub.airflightslist.model.User user) {
        super(username, password, authorities);
        this.user = user;
    }

    public pl.jgora.aeroklub.airflightslist.model.User getUser() {
        return user;
    }
}
