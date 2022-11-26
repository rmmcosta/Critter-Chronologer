package com.udacity.jdnd.course3.critter.pet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
@Data
public class PetDTO {
    private long id;
    private PetType type;
    private String name;
    private long ownerId;
    private LocalDate birthDate;
    private String notes;
    private Set<Behavior> behaviors;
}
