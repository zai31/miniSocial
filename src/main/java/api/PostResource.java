package api;

import Domain.User;
import Services.UserServices.PostService;
import app.DTO.CommentDTO;
import app.DTO.PostDTO;
import app.DTO.createPostDTO;
import Domain.Post;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.time.LocalDateTime;
import java.util.List;


@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {


    @EJB
    private PostService postService;
    @PersistenceContext
    private EntityManager em;

    @POST
    public Response createPost(PostDTO postDTO) {
        try {
            postService.createPost(
                    postDTO.getAuthorId(),
                    postDTO.getContent(),
                    postDTO.getImageUrl(),
                    postDTO.getLink()
            );
            return Response.status(Response.Status.CREATED)
                    .entity("Post created successfully.")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create post.")
                    .build();
        }
    }

    @GET
    @Path("/feed")
    public Response feed(@QueryParam("userId") Long userId) {
        User user = em.find(User.class, userId);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();

        List<PostDTO> feed = postService.getFeed(userId);
        return Response.ok(feed).build();
    }


    @PUT
    @Path("/{postId}")
    public Response update(@PathParam("postId") Long postId, PostDTO dto) {
        Post post = em.find(Post.class, postId);
        if (post == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();

        postService.updatePost(postId, dto.getContent(), dto.getImageUrl(), dto.getLink());
        return Response.ok("Post updated successfully.").build();
    }

    @DELETE
    @Path("/{postId}")
    public Response delete(@PathParam("postId") Long postId) {
        Post post = em.find(Post.class, postId);
        if (post == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();

        postService.deletePost(postId);
        return Response.ok("Post deleted successfully.").build();
    }

    @POST
    @Path("/{postId}/comment")
    public Response comment(@PathParam("postId") Long postId, CommentDTO dto) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, dto.userId);

        if (post == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();

        postService.addComment(postId, dto.userId, dto.text);
        return Response.ok("Comment added successfully.").build();
    }

    @POST
    @Path("/{postId}/like")
    public Response like(@PathParam("postId") Long postId, @QueryParam("userId") Long userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);

        if (post == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();

        postService.likePost(postId, userId);
        return Response.ok("Post liked successfully.").build();
    }
}

