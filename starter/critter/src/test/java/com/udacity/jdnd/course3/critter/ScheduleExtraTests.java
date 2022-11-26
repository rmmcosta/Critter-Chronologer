package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleController;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.schedule.ScheduleInvalidException;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleExtraTests {
    @LocalServerPort
    int port;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    CustomerService customerService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void invalidSchedulingThrowsException() {

        Customer customer = new Customer();
        customer.setName("Customer 1");
        customer.setNotes("xpto");
        customer.setPhoneNumber("1234-556");
        customer = customerService.saveCustomer(customer);
        Pet pet = new Pet();
        pet.setOwner(customer);
        pet.setType(PetType.CAT);
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setNotes("ai e tal");
        pet.setName("Puskas");
        pet.setBehaviors(new HashSet<>());
        pet = petService.savePet(pet);
        Employee employee = new Employee();
        employee.setAvailability(Set.of(DayOfWeek.MONDAY));
        employee.setSkills(Set.of(EmployeeSkill.PETTING));
        employee.setName("Gervásio");
        employee = employeeService.saveEmployee(employee);
        Schedule schedule = new Schedule();
        schedule.setScheduleDate(LocalDate.now());
        schedule.setStartTime(LocalTime.of(9,0));
        schedule.setEndTime(LocalTime.of(10,0));
        schedule.setPets(List.of(pet));
        schedule.setEmployees(List.of(employee));
        schedule.setActivities(Set.of(EmployeeSkill.PETTING));
        //invalid date, data must be at least the current day + 1
        assertThrows(ScheduleInvalidException.class, () -> scheduleService.saveSchedule(schedule), "Invalid Date");
        schedule.setScheduleDate(LocalDate.now().plusDays(1));
        schedule.setPets(new ArrayList<>());
        //empty pet list
        assertThrows(ScheduleInvalidException.class, () -> scheduleService.saveSchedule(schedule), "Empty Pet List");
        schedule.setPets(List.of(pet));
        schedule.setEmployees(new ArrayList<>());
        //empty employees list
        assertThrows(ScheduleInvalidException.class, () -> scheduleService.saveSchedule(schedule), "Empty Employee List");
        schedule.setEmployees(List.of(employee));
        schedule.setActivities(new HashSet<>());
        //no activities
        assertThrows(ScheduleInvalidException.class, () -> scheduleService.saveSchedule(schedule), "No Activities");
        schedule.setActivities(Set.of(EmployeeSkill.PETTING));
        //everything ok
        assertDoesNotThrow(() -> scheduleService.saveSchedule(schedule));

        scheduleService.deleteSchedule(schedule.getId());
        employeeService.deleteEmployee(employee.getId());
        petService.deletePet(pet.getId());
        customerService.deleteCustomer(customer.getId());
    }

    @Test
    public void invalidScheduleGivesBadRequest() {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(LocalDate.now());
        scheduleDTO.setPetIds(new ArrayList<>());
        scheduleDTO.setEmployeeIds(new ArrayList<>());
        scheduleDTO.setActivities(new HashSet<>());
        String url = "http://localhost:" + port + "/schedule";
        ResponseEntity<ScheduleDTO> responseEntity = restTemplate.postForEntity(url, scheduleDTO, ScheduleDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
