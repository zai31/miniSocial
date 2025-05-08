package api;

import Domain.Connection;
import Domain.User;
import Services.UserServices.ConnectionService;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/connections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConnectionResource {

    @EJB
    private ConnectionService connectionService;

    @POST
    @Path("/send")
    public Response sendRequest(@QueryParam("senderId") Long senderId, @QueryParam("receiverId") Long receiverId) {
        User sender = connectionService.findUserById(senderId);
        User receiver = connectionService.findUserById(receiverId);

        if (sender == null || receiver == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid senderId or receiverId.")
                    .build();
        }

        connectionService.sendFriendRequest(senderId, receiverId);
        return Response.ok("Friend request sent.").build();
    }

    @POST
    @Path("/respond")
    public Response respondToRequest(@QueryParam("requestId") Long requestId, @QueryParam("accept") boolean accept) {
        connectionService.respondToRequest(requestId, accept);
        return Response.ok("Response recorded.").build();
    }

    @GET
    @Path("/pending")
    public List<Connection> getPending(@QueryParam("userId") Long userId) {
        return connectionService.getPendingRequests(userId);
    }

    @GET
    @Path("/friends")
    public List<User> getFriends(@QueryParam("userId") Long userId) {
        System.out.println("connectionService = " + connectionService);
        return connectionService.getFriends(userId);
    }
}
