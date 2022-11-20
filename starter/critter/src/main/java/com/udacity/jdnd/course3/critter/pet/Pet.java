package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    private PetType type;
    @Column(length = 128)
    @Nationalized
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;
    private LocalDate birthDate;
    @Column(length = 5000)
    @Nationalized
    private String notes;
    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules;
}
