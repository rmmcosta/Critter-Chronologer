package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeTimeslotTests {
    @Autowired
    CustomerService customerService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ScheduleService scheduleService;

    @Test
    public void threeAvailableOfFiveEmployeesByTimeslot() {
        Customer customer = new Customer();
        customer.setPhoneNumber("343-3434");
        customer.setName("Helton");
        customer.setNotes("doesn't matter");
        customer = customerService.saveCustomer(customer);

        Pet pet = new Pet();
        pet.setName("Peúgas");
        pet.setOwner(customer);
        pet.setBirthDate(LocalDate.now());
        pet.setType(PetType.CAT);
        pet.setNotes("bla, bla, bla");
        pet = petService.savePet(pet);

        Employee employee1 = new Employee();
        employee1.setAvailability(Set.of(DayOfWeek.MONDAY));
        employee1.setSkills(Set.of(EmployeeSkill.PETTING));
        employee1.setName("Carlos");
        employee1 = employeeService.saveEmployee(employee1);
        final Long employee1Id = employee1.getId();

        Employee employee2 = new Employee();
        employee2.setAvailability(Set.of(DayOfWeek.MONDAY));
        employee2.setSkills(Set.of(EmployeeSkill.PETTING));
        employee2.setName("Joaquim");
        employee2 = employeeService.saveEmployee(employee2);
        final Long employee2Id = employee2.getId();

        Employee employee3 = new Employee();
        employee3.setAvailability(Set.of(DayOfWeek.TUESDAY));
        employee3.setSkills(Set.of(EmployeeSkill.PETTING));
        employee3.setName("César");
        employee3 = employeeService.saveEmployee(employee3);
        final Long employee3Id = employee3.getId();

        Employee employee4 = new Employee();
        employee4.setAvailability(Set.of(DayOfWeek.MONDAY));
        employee4.setSkills(Set.of(EmployeeSkill.PETTING));
        employee4.setName("John");
        employee4 = employeeService.saveEmployee(employee4);
        final Long employee4Id = employee4.getId();

        Employee employee5 = new Employee();
        employee5.setAvailability(Set.of(DayOfWeek.WEDNESDAY));
        employee5.setSkills(Set.of(EmployeeSkill.PETTING));
        employee5.setName("Charles");
        employee5 = employeeService.saveEmployee(employee5);
        final Long employee5Id = employee5.getId();

        Schedule schedule1 = new Schedule();
        schedule1.setEmployees(List.of(employee1));
        schedule1.setScheduleDate(LocalDate.now().plusDays(1));
        schedule1.setPets(List.of(pet));
        schedule1.setActivities(Set.of(EmployeeSkill.PETTING));
        schedule1.setStartTime(LocalTime.of(8, 30));
        schedule1.setEndTime(LocalTime.of(9, 30));
        schedule1 = scheduleService.saveSchedule(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setEmployees(List.of(employee2));
        schedule2.setScheduleDate(LocalDate.now().plusDays(1));
        schedule2.setPets(List.of(pet));
        schedule2.setActivities(Set.of(EmployeeSkill.PETTING));
        schedule2.setStartTime(LocalTime.of(9, 30));
        schedule2.setEndTime(LocalTime.of(10, 30));
        schedule2 = scheduleService.saveSchedule(schedule2);

        Schedule schedule3 = new Schedule();
        schedule3.setEmployees(List.of(employee4));
        schedule3.setScheduleDate(LocalDate.now().plusDays(1));
        schedule3.setPets(List.of(pet));
        schedule3.setActivities(Set.of(EmployeeSkill.PETTING));
        schedule3.setStartTime(LocalTime.of(12, 0));
        schedule3.setEndTime(LocalTime.of(14, 0));
        schedule3 = scheduleService.saveSchedule(schedule3);

        List<Employee> availableEmployeesByTimeslot
                = employeeService.getAvailableEmployeesByTimeslot(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalDate.now().plusDays(1)
        );

        assertEquals(3, availableEmployeesByTimeslot.size());

        assertFalse(availableEmployeesByTimeslot.stream().anyMatch(employee -> Objects.equals(employee.getId(), employee1Id)));
        assertFalse(availableEmployeesByTimeslot.stream().anyMatch(employee -> Objects.equals(employee.getId(), employee2Id)));
        assertTrue(availableEmployeesByTimeslot.stream().anyMatch(employee -> Objects.equals(employee.getId(), employee3Id)));
        assertTrue(availableEmployeesByTimeslot.stream().anyMatch(employee -> Objects.equals(employee.getId(), employee4Id)));
        assertTrue(availableEmployeesByTimeslot.stream().anyMatch(employee -> Objects.equals(employee.getId(), employee5Id)));

        scheduleService.deleteSchedule(schedule1.getId());
        scheduleService.deleteSchedule(schedule2.getId());
        scheduleService.deleteSchedule(schedule3.getId());
        employeeService.deleteEmployee(employee1.getId());
        employeeService.deleteEmployee(employee2.getId());
        employeeService.deleteEmployee(employee3.getId());
        employeeService.deleteEmployee(employee4.getId());
        employeeService.deleteEmployee(employee5.getId());
        petService.deletePet(pet.getId());
        customerService.deleteCustomer(customer.getId());
    }
}
