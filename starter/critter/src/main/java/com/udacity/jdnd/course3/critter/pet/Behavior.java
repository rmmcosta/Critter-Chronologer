package com.udacity.jdnd.course3.critter.pet;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Behavior {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 128)
    private String name;
    @Column(length = 512)
    private String description;
    @ElementCollection
    @JoinTable(
            name = "PetType_Behavior"
    )
    @Column(name = "pet_type")
    private List<PetType> petTypes;
    @ManyToMany(mappedBy = "behaviors")
    Set<Pet> pets;
}
