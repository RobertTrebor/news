package de.gainc.news;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NEWSUSER")
public class NewsUser {

  @NotBlank
  private String firstname;

  private String lastname;

  private LocalDate birthday;

  @Id
  // @Length(min = 4, message = "Muss mind. 4 Zeichen lang sein.")
  private String username;

  private String password;

}
