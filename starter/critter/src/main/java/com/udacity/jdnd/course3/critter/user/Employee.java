package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 128)
    @Nationalized
    private String name;
    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules;
    @ElementCollection
    @JoinTable(
            name = "employee_skill"
    )
    @Column(name = "skill")
    private Set<EmployeeSkill> skills;
    @ElementCollection
    @JoinTable(
            name = "employee_availability"
    )
    private Set<DayOfWeek> availability;
}
