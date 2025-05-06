package Services.UserServices;

import app.DTO.createPostDTO;
import Domain.Post;
import Domain.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

@Stateless
public class PostService {

    @PersistenceContext
    private EntityManager em;

    public Post createPost(String userEmail, createPostDTO dto) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", userEmail)
                .getSingleResult();

        Post post = new Post();
        post.setAuthor(user);
        post.setContent(dto.content);
        post.setLink(dto.link);
        post.setTimestamp(LocalDateTime.now());

        if (dto.imageBase64 != null && !dto.imageBase64.isEmpty()) {
            String imageUrl = saveImage(dto.imageBase64); // Simulated
            post.setImageUrl(imageUrl);
        }

        em.persist(post);
        return post;
    }

    private String saveImage(String base64) {
        // Simulate image storage logic
        return "http://cdn.example.com/images/" + System.currentTimeMillis() + ".jpg";
    }
}
