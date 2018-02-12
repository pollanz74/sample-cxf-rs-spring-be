package org.pollanz.samples.api.core.jpa;

import org.pollanz.samples.api.core.domain.Pet;
import org.pollanz.samples.api.core.pojo.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByStatusIn(List<Status> statuses);

    List<Pet> findByTagIn(List<String> tags);

}
