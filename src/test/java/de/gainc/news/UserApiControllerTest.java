package de.gainc.news;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserApiController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class UserApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NewsUserRepository userRepository;

  private NewsUser testUser;


  @Before
  public void setup() {
    testUser = new NewsUser(
        "testfirst", "testlast",
        LocalDate.of(1970, 1, 1),
        "testuser",
        "testpass");
    when(userRepository.findAll())
        .thenReturn(Arrays.asList(testUser));
    when(userRepository.findById(testUser.getUsername()))
        .thenReturn(Optional.of(testUser));
    when(userRepository.save(any()))
        .thenReturn(testUser);
  }

  @Test
  public void findAll() throws Exception {
    mockMvc.perform(get("/api"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$[0].username")
            .value(testUser.getUsername()))
        .andExpect(jsonPath("$[0].firstname")
            .value(testUser.getFirstname()))
        .andExpect(jsonPath("$[0].lastname")
            .value(testUser.getLastname()))
        .andExpect(jsonPath("$[0].password")
            .value(testUser.getPassword()))
        .andExpect(jsonPath("$[0].birthday")
            .value(testUser.getBirthday().toString()));

    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void findAll2() throws Exception {
    mockMvc.perform(get("/api"))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentType("application/json;charset=UTF-8"))

        .andDo(document("findone", responseFields(
            fieldWithPath("[]username").description("The users name"),
            fieldWithPath("[]firstname").description("The users first name"),
            fieldWithPath("[]lastname").description("The users last name"),
            fieldWithPath("[]birthday").description("The users birthday"),
            fieldWithPath("[]password").description("Passwordr")
        )));
  }




  @Test
  public void save() throws Exception{
    String json = "{\"username\":\"testuser\"," +
        "\"firstname\":\"testfirst\"," +
        "\"lastname\":\"testlast\"," +
        "\"password\":\"testpass\"," +
        "\"birthday\":\"1970-01-01\"" +
        "}";
    mockMvc.perform(post("/api")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());

    ArgumentCaptor<NewsUser> argument = ArgumentCaptor.forClass(NewsUser.class);
    verify(userRepository, times(1)).save(argument.capture());
    NewsUser savedUser = argument.getValue();
    assertThat(savedUser.getUsername()).isEqualTo("testuser");
    assertThat(savedUser.getFirstname()).isEqualTo("testfirst");
    assertThat(savedUser.getLastname()).isEqualTo("testlast");
    assertThat(savedUser.getBirthday()).isEqualTo(LocalDate.of(1970, 1, 1));

    assertTrue(new BCryptPasswordEncoder().matches("testpass", savedUser.getPassword()));
  }

  @Test
  public void testResponseEntity() throws Exception{

    String json = "{\"username\":\"testuser\"," +
        "\"firstname\":\"testfirst\"," +
        "\"lastname\":\"testlast\"," +
        "\"password\":\"testpass\"," +
        "\"birthday\":\"1970-01-01\"" +
        "}";

    mockMvc.perform(post("/api")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(header()
            .string("Location", "http://localhost:8080/api/testuser"));
  }



}