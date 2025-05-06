package Domain;

import Domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum for 'user' or 'admin'

    // Getters and Setters
}
