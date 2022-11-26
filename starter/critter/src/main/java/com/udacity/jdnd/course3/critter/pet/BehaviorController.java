package com.udacity.jdnd.course3.critter.pet;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.service.BehaviorService;
import com.udacity.jdnd.course3.critter.views.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/behavior")
public class BehaviorController {
    @Autowired
    BehaviorService behaviorService;

    @JsonView(Views.Public.class)
    @PostMapping
    public Behavior saveBehavior(@RequestBody Behavior behavior) {
        return behaviorService.saveBehavior(behavior);
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public List<Behavior> getAllBehaviors() {
        return behaviorService.getAllBehaviors();
    }

    @JsonView(Views.Public.class)
    @GetMapping("/petType")
    public List<Behavior> getAllBehaviorsByPetType(@RequestParam String petType) {
        return behaviorService.getAllBehaviorsByPetType(PetType.valueOf(petType));
    }

    @JsonView(Views.Public.class)
    @GetMapping("/{id}")
    public Behavior getBehavior(@PathVariable Long id) {
        return behaviorService.getBehaviorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBehavior(@PathVariable Long id) {
        behaviorService.deleteBehavior(id);
    }
}
