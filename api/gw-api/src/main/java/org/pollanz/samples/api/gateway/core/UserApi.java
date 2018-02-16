package org.pollanz.samples.api.gateway.core;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.pollanz.samples.api.gateway.core.pojo.LoginVM;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(UserApi.ROOT_PATH)
@Api(value = UserApi.ROOT_PATH, description = "")
public interface UserApi {

    String ROOT_PATH = "/user";

    @POST
    @Path("/authenticate")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Authenticate users", tags = {"user",})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "authentication failed")})
    Response authenticate(LoginVM loginVM);

}
