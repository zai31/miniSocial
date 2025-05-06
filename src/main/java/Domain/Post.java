package Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_table")

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id")
    private User author;

    @Column(length = 1000)
    private String content;

    private String imageUrl;
    private String link;

    private LocalDateTime timestamp;
}
