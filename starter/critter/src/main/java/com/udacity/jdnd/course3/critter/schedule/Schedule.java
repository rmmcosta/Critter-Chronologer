package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate scheduleDate;
    @ManyToMany
    @JoinTable(
            name = "schedule_employee",
            joinColumns = { @JoinColumn(name = "schedule_id")},
            inverseJoinColumns = { @JoinColumn(name = "employee_id")}
    )
    List<Employee> employees;
    @ManyToMany
    @JoinTable(
            name = "schedule_pet",
            joinColumns = { @JoinColumn(name = "schedule_id")},
            inverseJoinColumns = { @JoinColumn(name = "pet_id")}
    )
    List<Pet> pets;
    @ElementCollection
    @JoinTable(
            name = "schedule_activity"
    )
    @Column(name = "activity")
    private Set<EmployeeSkill> activities;
}
