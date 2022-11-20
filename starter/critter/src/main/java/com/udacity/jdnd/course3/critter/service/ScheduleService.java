package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedules() {
        return (List<Schedule>) scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petId) {
        return scheduleRepository.findSchedulesByPets_Id(petId);
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findSchedulesByEmployees_Id(employeeId);
    }

    public List<Schedule> getScheduleForCustomer(Long customerId) {
        return scheduleRepository.findSchedulesByPets_OwnerId(customerId);
    }
}
