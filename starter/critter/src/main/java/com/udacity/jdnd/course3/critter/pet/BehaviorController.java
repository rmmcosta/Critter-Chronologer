package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.BehaviorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/behavior")
public class BehaviorController {
    @Autowired
    BehaviorService behaviorService;

    @PostMapping
    public BehaviorDTO saveBehavior(@RequestBody BehaviorDTO behaviorDTO) {
        return behaviorToBehaviorDto(behaviorService.saveBehavior(behaviorDtoToBehavior(behaviorDTO)));
    }

    @GetMapping
    public List<BehaviorDTO> getAllBehaviors() {
        return behaviorService.getAllBehaviors()
                .stream()
                .map(this::behaviorToBehaviorDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/petType")
    public List<BehaviorDTO> getAllBehaviorsByPetType(@RequestParam String petType) {
        return behaviorService.getAllBehaviorsByPetType(PetType.valueOf(petType))
                .stream()
                .map(this::behaviorToBehaviorDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BehaviorDTO getBehavior(@PathVariable Long id) {
        return behaviorToBehaviorDto(behaviorService.getBehaviorById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteBehavior(@PathVariable Long id) {
        behaviorService.deleteBehavior(id);
    }

    private Behavior behaviorDtoToBehavior(BehaviorDTO behaviorDTO) {
        Behavior behavior = new Behavior();
        BeanUtils.copyProperties(behaviorDTO, behavior);
        return behavior;
    }

    private BehaviorDTO behaviorToBehaviorDto(Behavior behavior) {
        BehaviorDTO behaviorDto = new BehaviorDTO();
        BeanUtils.copyProperties(behavior, behaviorDto);
        return behaviorDto;
    }
}
