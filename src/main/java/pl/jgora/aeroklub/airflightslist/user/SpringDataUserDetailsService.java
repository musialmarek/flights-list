package pl.jgora.aeroklub.airflightslist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.jgora.aeroklub.airflightslist.model.Role;
import pl.jgora.aeroklub.airflightslist.model.User;

import java.util.HashSet;
import java.util.Set;

public class SpringDataUserDetailsService implements UserDetailsService {
    private UserService userService;
    @Autowired
    public void setUserRepository(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.findByLogin(login);
        if (user == null) {throw new UsernameNotFoundException(login); }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role role = user.getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        return new CurrentUser(user.getLogin(),user.getPassword(),
                grantedAuthorities, user);

    }
}
