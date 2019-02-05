package de.gainc.news;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
public class RegistrationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NewsUserRepository newsUserRepository;


  @Test
  public void testShowRegistrationForm() throws Exception {
    mockMvc
        .perform(get("/user/register"))
        .andExpect(model().attributeExists("newsUser"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Register")));
  }

  @Test
  public void testthis() throws Exception {
    NewsUser checkUser = new NewsUser("test", "test",
        LocalDate.of(1970, 1, 1), "user", "pass");

    mockMvc.perform(post("/user/register")
        .with(csrf())
        .param("firstname", "test")
        .param("lastname", "test")
        .param("birthday", "1970-01-01")
        .param("username", "user")
        .param("password", "pass"))
        .andExpect(redirectedUrl("/user/profile/user"));

    ArgumentCaptor<NewsUser> argument = ArgumentCaptor.forClass(NewsUser.class);
    verify(newsUserRepository, times(1)).save(argument.capture());
    NewsUser savedUser = argument.getValue();

    assertThat(savedUser.getUsername()).isEqualTo("user");
    assertThat(savedUser.getFirstname()).isEqualTo("test");
    assertThat(savedUser.getLastname()).isEqualTo("test");
    assertThat(savedUser.getBirthday()).isEqualTo(LocalDate.of(1970, 1, 1));

    assertTrue(new BCryptPasswordEncoder().matches("pass", savedUser.getPassword()));
  }

  @Test @Ignore
  public void testNewUser() throws Exception{
    NewsUser checkUser = new NewsUser("test", "test",
        LocalDate.of(1970, 11, 11), "user", "pass");
    mockMvc.perform(post("/user/register")
        .with(csrf())
            .param("firstname", "test")
            .param("lastname", "test")
            .param("birthday", "1970-11-11")
            .param("username", "user")
            .param("password", "pass"))
        .andExpect(redirectedUrl("/user/profile/user"))
        .andExpect(flash().attributeExists("newsUser"));
    verify(newsUserRepository, times(1)).save(checkUser);
  }

  @Test
  public void testValidation() throws Exception{
    NewsUser checkUser = new NewsUser("a", "b",
        LocalDate.of(1970, 1, 1), "use", "pass");

    mockMvc
        .perform(post("/user/register")
            .with(csrf())
            .param("firstname", "a")
            .param("lastname", "b")
            .param("birthday", "1970-01-01")
            .param("username", "use")
            .param("password", "pass"))
        .andExpect(MockMvcResultMatchers.view().name("register"))
        .andExpect(content().string(
            containsString("Muss mind. 4 Zeichen lang sein.")));
    verify(newsUserRepository, times(0)).save(checkUser);
  }

}