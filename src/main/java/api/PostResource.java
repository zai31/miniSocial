package api;

import Services.UserServices.PostService;
import app.DTO.createPostDTO;
import Domain.Post;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;




@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    private PostService postService;

    @Context
    SecurityContext securityContext;

   @POST
   @Path("/post")

//@RolesAllowed({"USER"}) // disable temporarily
    public Response createPost(createPostDTO dto) {
        // Temporarily hardcode a user
        String userEmail = "test@example.com";

        Post post = postService.createPost(userEmail, dto);
        return Response.status(Response.Status.CREATED).entity(post).build();
    }

}
