package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllEntitiesCRUDTests {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void allCrudEndpointsWorkingWithSuccess() {
        String uri = "http://localhost:" + port;
        //create customer
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Rambo");
        customerDTO.setPhoneNumber("1345-654");
        customerDTO.setNotes("no notes taken");
        ResponseEntity<CustomerDTO> customerDTOResponseEntity = restTemplate.postForEntity(uri + "/user/customer", customerDTO, CustomerDTO.class);
        assertEquals(HttpStatus.OK, customerDTOResponseEntity.getStatusCode());
        customerDTO.setId(Objects.requireNonNull(customerDTOResponseEntity.getBody()).getId());
        //create pet
        PetDTO petDTO = new PetDTO();
        petDTO.setBirthDate(LocalDate.now());
        petDTO.setOwnerId(customerDTO.getId());
        petDTO.setType(PetType.CAT);
        petDTO.setNotes("no notes taken");
        petDTO.setName("Miau");
        ResponseEntity<PetDTO> petDTOResponseEntity = restTemplate.postForEntity(uri + "/pet", petDTO, PetDTO.class);
        assertEquals(HttpStatus.OK, petDTOResponseEntity.getStatusCode());
        petDTO.setId(Objects.requireNonNull(petDTOResponseEntity.getBody()).getId());
        //create employee
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDaysAvailable(Set.of(DayOfWeek.MONDAY));
        employeeDTO.setName("Tarzan");
        employeeDTO.setSkills(Set.of(EmployeeSkill.PETTING));
        ResponseEntity<EmployeeDTO> employeeDTOResponseEntity = restTemplate.postForEntity(uri + "/user/employee", employeeDTO, EmployeeDTO.class);
        assertEquals(HttpStatus.OK, employeeDTOResponseEntity.getStatusCode());
        employeeDTO.setId(Objects.requireNonNull(employeeDTOResponseEntity.getBody()).getId());
        //create schedule
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setActivities(Set.of(EmployeeSkill.PETTING));
        scheduleDTO.setPetIds(List.of(petDTO.getId()));
        scheduleDTO.setDate(LocalDate.now().plusDays(3));
        scheduleDTO.setEmployeeIds(List.of(employeeDTO.getId()));
        ResponseEntity<ScheduleDTO> scheduleDTOResponseEntity = restTemplate.postForEntity(uri + "/schedule", scheduleDTO, ScheduleDTO.class);
        assertEquals(HttpStatus.OK, scheduleDTOResponseEntity.getStatusCode());
        scheduleDTO.setId(Objects.requireNonNull(scheduleDTOResponseEntity.getBody()).getId());

        customerDTO.setNotes("This customer is a cat lover");
        restTemplate.put(uri + "/user/customer/", customerDTO);
        customerDTOResponseEntity = restTemplate.getForEntity(uri + "/user/customer/" + customerDTO.getId(), CustomerDTO.class);
        assertEquals(HttpStatus.OK, customerDTOResponseEntity.getStatusCode());
        assertEquals(customerDTO.getNotes(), Objects.requireNonNull(customerDTOResponseEntity.getBody()).getNotes());

        petDTO.setNotes("This pet is so cute");
        restTemplate.put(uri + "/pet", petDTO);
        petDTOResponseEntity = restTemplate.getForEntity(uri + "/pet/" + petDTO.getId(), PetDTO.class);
        assertEquals(HttpStatus.OK, petDTOResponseEntity.getStatusCode());
        assertEquals(petDTO.getNotes(), Objects.requireNonNull(petDTOResponseEntity.getBody()).getNotes());

        employeeDTO.setSkills(Set.of(EmployeeSkill.PETTING, EmployeeSkill.SHAVING));
        restTemplate.put(uri + "/user/employee", employeeDTO);
        employeeDTOResponseEntity = restTemplate.getForEntity(uri + "/user/employee/" + employeeDTO.getId(), EmployeeDTO.class);
        assertEquals(HttpStatus.OK, employeeDTOResponseEntity.getStatusCode());
        assertEquals(employeeDTO.getSkills().size(), Objects.requireNonNull(employeeDTOResponseEntity.getBody()).getSkills().size());

        scheduleDTO.setDate(LocalDate.now().plusDays(5));
        restTemplate.put(uri + "/schedule", scheduleDTO);
        scheduleDTOResponseEntity = restTemplate.getForEntity(uri + "/schedule/" + scheduleDTO.getId(), ScheduleDTO.class);
        assertEquals(HttpStatus.OK, scheduleDTOResponseEntity.getStatusCode());
        assertEquals(0, scheduleDTO.getDate().compareTo(Objects.requireNonNull(scheduleDTOResponseEntity.getBody()).getDate()));

        //confirm schedule exists, delete and confirm not exists anymore
        scheduleDTOResponseEntity = restTemplate.getForEntity(uri + "/schedule/" + scheduleDTO.getId(), ScheduleDTO.class);
        assertEquals(HttpStatus.OK, scheduleDTOResponseEntity.getStatusCode());
        restTemplate.delete(uri + "/schedule/" + scheduleDTO.getId());
        scheduleDTOResponseEntity = restTemplate.getForEntity(uri + "/schedule/" + scheduleDTO.getId(), ScheduleDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, scheduleDTOResponseEntity.getStatusCode());

        //confirm employee exists, delete and confirm not exists anymore
        employeeDTOResponseEntity = restTemplate.getForEntity(uri + "/user/employee/" + employeeDTO.getId(), EmployeeDTO.class);
        assertEquals(HttpStatus.OK, employeeDTOResponseEntity.getStatusCode());
        restTemplate.delete(uri + "/user/employee/" + employeeDTO.getId());
        employeeDTOResponseEntity = restTemplate.getForEntity(uri + "/user/employee/" + employeeDTO.getId(), EmployeeDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, employeeDTOResponseEntity.getStatusCode());

        //confirm pet exists, delete and confirm not exists anymore
        petDTOResponseEntity = restTemplate.getForEntity(uri + "/pet/" + petDTO.getId(), PetDTO.class);
        assertEquals(HttpStatus.OK, petDTOResponseEntity.getStatusCode());
        restTemplate.delete(uri + "/pet/" + petDTO.getId());
        petDTOResponseEntity = restTemplate.getForEntity(uri + "/pet/" + petDTO.getId(), PetDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, petDTOResponseEntity.getStatusCode());

        //confirm customer exists, delete and confirm not exists anymore
        customerDTOResponseEntity = restTemplate.getForEntity(uri + "/user/customer/" + customerDTO.getId(), CustomerDTO.class);
        assertEquals(HttpStatus.OK, customerDTOResponseEntity.getStatusCode());
        restTemplate.delete(uri + "/user/customer/" + customerDTO.getId());
        customerDTOResponseEntity = restTemplate.getForEntity(uri + "/user/customer/" + customerDTO.getId(), CustomerDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, customerDTOResponseEntity.getStatusCode());

    }
}
