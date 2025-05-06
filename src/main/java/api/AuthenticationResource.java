package api;

import Domain.User;
import Services.UserServices.AuthenticationService;
import app.DTO.LoginDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    private AuthenticationService authenticationService;

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        try {
            User user = authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword());
            return Response.ok(user).build(); // You can return a JWT token for authentication
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
}
