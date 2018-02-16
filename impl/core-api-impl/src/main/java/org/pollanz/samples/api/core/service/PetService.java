package org.pollanz.samples.api.core.service;

import org.pollanz.samples.api.core.pojo.PetPojo;
import org.pollanz.samples.api.core.pojo.enumeration.Status;

import java.util.List;

public interface PetService {

    PetPojo addPet(PetPojo petPojo);

    void deletePet(Long id);

    List<PetPojo> findPetsByStatus(List<Status> status);

    List<PetPojo> findPetsByTags(List<String> tags);

    PetPojo findPetById(Long id);

    PetPojo updatePet(PetPojo petPojo);
}
