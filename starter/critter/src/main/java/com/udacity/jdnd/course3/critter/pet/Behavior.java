package com.udacity.jdnd.course3.critter.pet;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.views.Views;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Behavior {
    @Id
    @GeneratedValue
    @JsonView(Views.Public.class)
    private Long id;
    @Column(length = 128)
    @JsonView(Views.Public.class)
    private String name;
    @Column(length = 512)
    @JsonView(Views.Public.class)
    private String description;
    @ElementCollection
    @JoinTable(
            name = "PetType_Behavior"
    )
    @Column(name = "pet_type")
    @JsonView(Views.Public.class)
    private List<PetType> petTypes;
}
