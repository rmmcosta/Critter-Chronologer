package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetInvalidUpdateException;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleToScheduleDto(scheduleService.saveSchedule(scheduleDtoToSchedule(scheduleDTO)));
    }

    @PutMapping
    public void updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        if (!(scheduleDTO.getId() > 0)) {
            throw new ScheduleInvalidUpdateException();
        }
        scheduleService.saveSchedule(scheduleDtoToSchedule(scheduleDTO));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getSchedules().stream().map(this::scheduleToScheduleDto).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleForPet(petId).stream().map(this::scheduleToScheduleDto).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeId).stream().map(this::scheduleToScheduleDto).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.getScheduleForCustomer(customerId).stream().map(this::scheduleToScheduleDto).collect(Collectors.toList());
    }

    @GetMapping("/{scheduleId}")
    public ScheduleDTO getSchedule(@PathVariable long scheduleId) {
        return scheduleToScheduleDto(scheduleService.getSchedule(scheduleId));
    }

    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@PathVariable long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }

    private Schedule scheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setScheduleDate(scheduleDTO.getDate());
        schedule.setPets(scheduleDTO.getPetIds()
                .stream()
                .map(petId -> petService.getPet(petId))
                .collect(Collectors.toList()));
        schedule.setEmployees(scheduleDTO.getEmployeeIds()
                .stream()
                .map(employeeId -> employeeService.getEmployee(employeeId))
                .collect(Collectors.toList()));
        return schedule;
    }

    private ScheduleDTO scheduleToScheduleDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setDate(schedule.getScheduleDate());
        scheduleDTO.setPetIds(schedule.getPets()
                .stream()
                .map(Pet::getId)
                .collect(Collectors.toList()));
        scheduleDTO.setEmployeeIds(schedule.getEmployees()
                .stream()
                .map(Employee::getId)
                .collect(Collectors.toList()));
        return scheduleDTO;
    }
}
