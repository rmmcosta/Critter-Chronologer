package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Long> {
    List<Pet> findPetsByOwnerId(Long ownerId);
}
