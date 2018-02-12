package org.pollanz.samples.api.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.pollanz.samples.api.core.domain.Pet;
import org.pollanz.samples.api.core.jpa.PetRepository;
import org.pollanz.samples.api.core.pojo.PetPojo;
import org.pollanz.samples.api.core.pojo.enumeration.Status;
import org.pollanz.samples.api.core.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public PetPojo addPet(PetPojo petPojo) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petPojo, pet);
        pet = petRepository.save(pet);
        PetPojo createdPetPojo = new PetPojo();
        BeanUtils.copyProperties(pet, createdPetPojo);
        return createdPetPojo;
    }

    @Override
    public void deletePet(Long id) {
        petRepository.delete(id);
    }

    @Override
    public List<PetPojo> findPetsByStatus(List<Status> status) {
        return petRepository.findByStatusIn(status).stream().map(p -> {
            PetPojo petPojo = new PetPojo();
            BeanUtils.copyProperties(p, petPojo);
            return petPojo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PetPojo> findPetsByTags(List<String> tags) {
        return petRepository.findByTagIn(tags).stream().map(p -> {
            PetPojo petPojo = new PetPojo();
            BeanUtils.copyProperties(p, petPojo);
            return petPojo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetPojo findPetById(Long id) {
        Pet pet = petRepository.findOne(id);
        PetPojo petPojo = null;
        if (pet != null) {
            petPojo = new PetPojo();
            BeanUtils.copyProperties(pet, petPojo);
        }

        return petPojo;
    }

    @Override
    public PetPojo updatePet(PetPojo petPojo) {
        Pet pet = petRepository.findOne(petPojo.getId());
        if (pet == null) {
            return null;
        }
        BeanUtils.copyProperties(petPojo, pet);
        pet = petRepository.save(pet);
        PetPojo updatedPetPojo = new PetPojo();
        BeanUtils.copyProperties(pet, updatedPetPojo);
        return updatedPetPojo;
    }

}
