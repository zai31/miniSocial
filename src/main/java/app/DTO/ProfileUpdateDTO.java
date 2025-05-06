package app.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateDTO {

    private String name;
    private String bio;
    private String email;
    private String password;

    // Getters and Setters
}

