package de.gainc.news;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class RegistrationController {

  @Autowired
  private NewsUserRepository newsUserRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/register")
  public String showRegistration(Model model){
    model.addAttribute("newsUser", new NewsUser());
    return "register";
  }

  @PostMapping("/register")
  public String processRegistration(@Valid NewsUser user,
      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("newsUser", user);
      model.addAttribute("errors", bindingResult.getFieldErrors());
      return "register";
    }
    String pass = passwordEncoder.encode(user.getPassword());
    user.setPassword(pass);

    newsUserRepository.save(user);
    redirectAttributes.addFlashAttribute("newsUser", user);
    return "redirect:/user/profile/" + user.getUsername();
  }


  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) throws IllegalArgumentException {
        try {
          setValue(LocalDate.parse(text,
              DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException x) {
          throw new IllegalArgumentException(x);
        }
      }
    });

  }

}
