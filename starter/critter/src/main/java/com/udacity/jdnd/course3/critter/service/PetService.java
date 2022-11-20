package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.PetRepository;
import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
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
}
