package Services.UserServices;

import Domain.Comment;
import app.DTO.PostDTO;
import app.DTO.createPostDTO;
import Domain.Post;
import Domain.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PostService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ConnectionService connectionService;

    @Transactional
    public void createPost(Long authorId, String content, String imageUrl, String link) {
        Post post = new Post();
        post.setAuthor(em.find(User.class, authorId));
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLink(link);
        post.setCreatedAt(LocalDateTime.now());
        em.persist(post);
    }

    @Transactional
    public List<PostDTO> getFeed(Long userId) {
        List<User> friends = connectionService.getFriends(userId);
        friends.add(em.find(User.class, userId));

        List<Post> posts = em.createQuery(
                        "SELECT DISTINCT p FROM Post p " +
                                "LEFT JOIN FETCH p.comments " +
                                "LEFT JOIN FETCH p.likes " +
                                "WHERE p.author IN :users " +
                                "ORDER BY p.createdAt DESC", Post.class)
                .setParameter("users", friends)
                .getResultList();

        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setAuthorId(post.getAuthor().getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            dto.setLink(post.getLink());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setAuthorUsername(post.getAuthor().getName());

            dto.setCommentContents(
                    post.getComments().stream()
                            .map(Comment::getText)
                            .collect(Collectors.toList())
            );

            dto.setLikesCount(post.getLikes().size());

            return dto;
        }).collect(Collectors.toList());
    }

    public void updatePost(Long postId, String content, String imageUrl, String link) {
        Post post = em.find(Post.class, postId);
        if (post != null) {
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setLink(link);
            post.setUpdatedAt(LocalDateTime.now());
            em.merge(post);
        }
    }

    public void deletePost(Long postId) {
        Post post = em.find(Post.class, postId);
        if (post != null) em.remove(post);
    }

    public void addComment(Long postId, Long userId, String text) {
        Comment comment = new Comment();
        comment.setAuthor(em.find(User.class, userId));
        comment.setPost(em.find(Post.class, postId));
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());
        em.persist(comment);
    }

    public void likePost(Long postId, Long userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        post.getLikes().add(user);
        em.merge(post);
    }
}
