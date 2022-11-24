package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.PetRepository;
import com.udacity.jdnd.course3.critter.pet.Behavior;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetInvalidException;
import com.udacity.jdnd.course3.critter.pet.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    BehaviorService behaviorService;

    public Pet savePet(Pet pet) {
        if (pet.getOwner() == null) {
            throw new PetInvalidException("Pet without Owner");
        }
        if (!allValidBehaviors(pet.getType(), pet.getBehaviors())) {
            throw new WrongBehaviorException();
        }
        return petRepository.save(pet);
    }

    private boolean allValidBehaviors(PetType petType, Set<Behavior> behaviors) {
        List<Behavior> allPetTypeBehaviors = behaviorService.getAllBehaviorsByPetType(petType);
        Set<Behavior> commonBehaviors = behaviors.stream()
                .filter(behavior -> allPetTypeBehaviors.stream().anyMatch(allTypeBehavior -> allTypeBehavior.getId().equals(behavior.getId())))
                .collect(Collectors.toSet());
        return commonBehaviors.size() == behaviors.size();
    }

    public Pet getPet(Long id) {
        return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    public List<Pet> getPets() {
        return (List<Pet>) petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findPetsByOwnerId(ownerId);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
