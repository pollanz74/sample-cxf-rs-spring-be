package org.pollanz.samples.api.gateway.core.proxy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.pollanz.samples.api.core.pojo.PetPojo;
import org.pollanz.samples.api.core.pojo.enumeration.Status;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(PetProxiedApi.ROOT_PATH)
@Api(value = PetProxiedApi.ROOT_PATH, description = "")
public interface PetProxiedApi {

    static final String ROOT_PATH = "/pet";

    @POST
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Add a new pet to the store", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 405, message = "Invalid input") })
    Response addPet(@Valid PetPojo petPojo);

    @DELETE
    @Path("/{petId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Deletes a pet", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found") })
    Response deletePet(@PathParam("petId") Long petId);

    @GET
    @Path("/findByStatus")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Finds Pets by status", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status value") })
    Response findPetsByStatus(@QueryParam("status") @NotNull List<Status > status);

    @GET
    @Path("/findByTags")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Finds Pets by tags", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid tag value") })
    Response findPetsByTags(@QueryParam("tags") @NotNull List<String> tags);

    @GET
    @Path("/{petId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Find pet by ID", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found") })
    Response getPetById(@PathParam("petId") Long petId);

    @PUT
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Update an existing pet", tags={ "pet",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found"),
        @ApiResponse(code = 405, message = "Validation exception") })
    Response updatePet(@Valid PetPojo petPojo);

}
