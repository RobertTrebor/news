package de.gainc.news;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class NewsUserDetailsService implements UserDetailsService {

  @Autowired
  private NewsUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    NewsUser user = userRepository.findByUsername(username);
    return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
  }

}
