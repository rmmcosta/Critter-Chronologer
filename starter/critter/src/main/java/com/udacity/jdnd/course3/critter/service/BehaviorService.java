package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.BehaviorRepository;
import com.udacity.jdnd.course3.critter.pet.Behavior;
import com.udacity.jdnd.course3.critter.pet.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviorService {
    @Autowired
    BehaviorRepository behaviorRepository;

    public List<Behavior> getAllBehaviorsByPetType(PetType petType) {
        return behaviorRepository.findBehaviorsByPetTypes(petType);
    }

    public Behavior saveBehavior(Behavior behavior) {
        return behaviorRepository.save(behavior);
    }

    public Behavior getBehaviorById(Long id) {
        return behaviorRepository.findById(id).orElseThrow(BehaviorNotFoundException::new);
    }

    public List<Behavior> getAllBehaviors() {
        return (List<Behavior>) behaviorRepository.findAll();
    }

    public void deleteBehavior(Long id) {
        behaviorRepository.deleteById(id);
    }
}
