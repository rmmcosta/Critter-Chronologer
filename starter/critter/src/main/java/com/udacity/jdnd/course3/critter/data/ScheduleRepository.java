package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    List<Schedule> findSchedulesByPets_Id(Long id);
    List<Schedule> findSchedulesByEmployees_Id(Long id);
    List<Schedule> findSchedulesByPets_OwnerId(Long OwnerId);
}
