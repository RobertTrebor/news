package de.gainc.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class NewsControllerAdvice {

  @ExceptionHandler(RuntimeException.class)
  public String handleException(RuntimeException x, Model model){
    log.error("Unhandled Exception", x);
    model.addAttribute("errormsg", x.getMessage());
    return "errorpage";
  }


}
