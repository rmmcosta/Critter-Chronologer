package com.udacity.jdnd.course3.critter.pet;

import lombok.Data;

import java.util.List;

@Data
public class BehaviorDTO {
    private Long id;
    private String name;
    private String description;
    private List<PetType> petTypes;
}
