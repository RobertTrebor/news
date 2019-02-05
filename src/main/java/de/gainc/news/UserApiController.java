package de.gainc.news;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class UserApiController {

  @Autowired
  private NewsUserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping
  public List<NewsUser> findAll(){
    return userRepository.findAll();
  }
/*
  @GetMapping("/{username}")
  public NewsUser findByUsername(@PathVariable("username") String username){
    return userRepository.findByUsername(username);
  }
*/
  @GetMapping("/{id}")
  public ResponseEntity<NewsUser> findById(@PathVariable("id") String id){
    return userRepository.findById(id)
        .map(u -> ResponseEntity.ok(u))
        .orElse(ResponseEntity.notFound().build());
  }

  /*
  @PostMapping
  public NewsUser save(@RequestBody NewsUser user){
    String pass = passwordEncoder.encode(user.getPassword());
    user.setPassword(pass);
    return userRepository.save(user);
  }
*/

  @PostMapping
  public ResponseEntity<NewsUser> save(@RequestBody NewsUser user){
    String pass = passwordEncoder.encode(user.getPassword());
    user.setPassword(pass);
    userRepository.save(user);
    //UriComponentsBuilder.fromUri()
    UriComponents uri = MvcUriComponentsBuilder
        .fromMethodCall(on(UserApiController.class).findById(user.getUsername())).build();
    return ResponseEntity.created(uri.toUri()).build();
  }
}
