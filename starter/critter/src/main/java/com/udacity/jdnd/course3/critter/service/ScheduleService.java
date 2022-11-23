package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule) {
        if (schedule.getScheduleDate() == null || schedule.getScheduleDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new ScheduleInvalidException("Invalid Date");
        }
        if (schedule.getPets().isEmpty()) {
            throw new ScheduleInvalidException("Empty Pet List");
        }
        if (schedule.getEmployees().isEmpty()) {
            throw new ScheduleInvalidException("Empty Employee List");
        }
        if (schedule.getActivities().isEmpty()) {
            throw new ScheduleInvalidException("No Activities");
        }
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedules() {
        return (List<Schedule>) scheduleRepository.findAll();
    }

    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
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

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
