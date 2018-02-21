package org.pollanz.samples.api.core.impl;

import com.codahale.metrics.annotation.Timed;
import org.pollanz.samples.api.core.PetApi;
import org.pollanz.samples.api.core.pojo.PetPojo;
import org.pollanz.samples.api.core.pojo.enumeration.Status;
import org.pollanz.samples.api.core.service.PetService;

import java.util.List;

public class PetApiServiceImpl implements PetApi {

    private final PetService petService;

    public PetApiServiceImpl(PetService petService) {
        this.petService = petService;
    }

    @Timed
    @Override
    public PetPojo addPet(PetPojo petPojo) {
        return petService.addPet(petPojo);
    }

    @Timed
    @Override
    public void deletePet(Long petId) {
        petService.deletePet(petId);
    }

    @Timed
    @Override
    public List<PetPojo> findPetsByStatus(List<Status> status) {
        return petService.findPetsByStatus(status);
    }

    @Timed
    @Override
    public List<PetPojo> findPetsByTags(List<String> tags) {
        return petService.findPetsByTags(tags);
    }

    @Timed
    @Override
    public PetPojo getPetById(Long petId) {
        return petService.findPetById(petId);
    }

    @Timed
    @Override
    public PetPojo updatePet(PetPojo petPojo) {
        return petService.updatePet(petPojo);
    }
}
