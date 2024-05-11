package controllers;

import exceptions.UnauthorizedException;
import services.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController
{
    private UserService userService;

    /**
     * Handles the "/login" route.
     * Here we authenticate a user based on username and
     * password. After that we add a token to it's column.
     *
     * @param request the request entity.
     * @return the request response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public Response login(@javax.websocket.server.PathParam("name") String requestName, @javax.websocket.server.PathParam("pass") String requestPass)
    {
        try {
            return Response
                .ok(userService.authenticate(requestName, requestPass))
                .build();
        }
        catch (UnauthorizedException e) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .build();
        }
    }

    /**
     * Injects the UserService.
     *
     * @param userService the UserService.
     */
    @Inject
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
