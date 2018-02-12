package org.pollanz.samples.api.gateway.impl.core.proxy;

import org.pollanz.samples.api.core.PetApi;
import org.pollanz.samples.api.core.pojo.PetPojo;
import org.pollanz.samples.api.core.pojo.enumeration.Status;
import org.pollanz.samples.api.gateway.core.proxy.PetProxiedApi;

import javax.ws.rs.core.Response;
import java.util.List;

public class PetProxiedApiServiceImpl implements PetProxiedApi {

    private final PetApi internalPetApi;

    public PetProxiedApiServiceImpl(PetApi internalPetApi) {
        this.internalPetApi = internalPetApi;
    }

    @Override
    public Response addPet(PetPojo petPojo) {
        PetPojo result = internalPetApi.addPet(petPojo);
        return Response.ok(result).build();
    }

    @Override
    public Response deletePet(Long petId) {
        internalPetApi.deletePet(petId);
        return Response.ok().build();
    }

    @Override
    public Response findPetsByStatus(List<Status> status) {
        List<PetPojo> result = internalPetApi.findPetsByStatus(status);
        return Response.ok(result).build();
    }

    @Override
    public Response findPetsByTags(List<String> tags) {
        List<PetPojo> result = internalPetApi.findPetsByTags(tags);
        return Response.ok(result).build();
    }

    @Override
    public Response getPetById(Long petId) {
        PetPojo result = internalPetApi.getPetById(petId);
        return Response.ok(result).build();
    }

    @Override
    public Response updatePet(PetPojo petPojo) {
        PetPojo result = internalPetApi.updatePet(petPojo);
        return Response.ok(result).build();
    }

}

