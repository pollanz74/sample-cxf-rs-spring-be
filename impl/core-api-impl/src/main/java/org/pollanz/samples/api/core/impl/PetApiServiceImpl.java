package org.pollanz.samples.api.core.impl;

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

    @Override
    public PetPojo addPet(PetPojo petPojo) {
        return petService.addPet(petPojo);
    }

    @Override
    public void deletePet(Long petId) {
        petService.deletePet(petId);
    }

    @Override
    public List<PetPojo> findPetsByStatus(List<Status> status) {
        return petService.findPetsByStatus(status);
    }

    @Override
    public List<PetPojo> findPetsByTags(List<String> tags) {
        return petService.findPetsByTags(tags);
    }

    @Override
    public PetPojo getPetById(Long petId) {
        return petService.findPetById(petId);
    }

    @Override
    public PetPojo updatePet(PetPojo petPojo) {
        return petService.updatePet(petPojo);
    }
}
