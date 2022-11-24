package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.pet.Behavior;
import com.udacity.jdnd.course3.critter.pet.PetType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BehaviorRepository extends CrudRepository<Behavior, Long> {
    List<Behavior> findBehaviorsByPetTypes(PetType petType);
}
