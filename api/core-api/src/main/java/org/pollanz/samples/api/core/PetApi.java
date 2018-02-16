package org.pollanz.samples.api.core;

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
import java.util.List;

@Path(PetApi.ROOT_PATH)
@Api(value = PetApi.ROOT_PATH, description = "")
public interface PetApi {

    static final String ROOT_PATH = "/sample-services/pet";

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new pet to the store", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 405, message = "Invalid input")})
    PetPojo addPet(@Valid PetPojo petPojo);

    @DELETE
    @Path("/{petId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Deletes a pet", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    void deletePet(@PathParam("petId") Long petId);

    @GET
    @Path("/findByStatus")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Finds Pets by status", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid status value")})
    List<PetPojo> findPetsByStatus(@QueryParam("status") @NotNull List<Status> status);

    @GET
    @Path("/findByTags")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Finds Pets by tags", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid tag value")})
    List<PetPojo> findPetsByTags(@QueryParam("tags") @NotNull List<String> tags);

    @GET
    @Path("/{petId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Find pet by ID", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = PetPojo.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    PetPojo getPetById(@PathParam("petId") Long petId);

    @PUT
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing pet", tags = {"pet",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    PetPojo updatePet(@Valid PetPojo petPojo);

}
