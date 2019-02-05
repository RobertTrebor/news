package de.gainc.news;


import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UnknownUserException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Slf4j
public class NewsUserController {

  private NewsUserRepository newsUserRepository;

  public NewsUserController(NewsUserRepository newsUserRepository){
    this.newsUserRepository = newsUserRepository;
  }

  @GetMapping("/profile/{username}")
  public String showProfile(Model model, @PathVariable("username") String username){
    if(!model.containsAttribute("newsUser")){
      log.info("Loading User");
      NewsUser user = newsUserRepository.findByUsername(username);
      if(user == null){
        throw new RuntimeException("User not found");
      }
      model.addAttribute("newsUser", user);
    }
    return "profile";
  }

}
