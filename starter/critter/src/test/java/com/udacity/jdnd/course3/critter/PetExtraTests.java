package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetInvalidException;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetExtraTests {
    @LocalServerPort
    int port;
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void petWithoutOwnerThrowsException() {
        Pet pet = new Pet();
        pet.setName("Xuxas");
        pet.setBirthDate(LocalDate.now());
        pet.setNotes("coiso et al");
        pet.setType(PetType.CAT);
        assertThrows(PetInvalidException.class, () -> petService.savePet(pet), "Pet without Owner");
        Customer customer = new Customer();
        customer.setName("Customer 1");
        customer.setNotes("xpto");
        customer.setPhoneNumber("1234-556");
        customer = customerService.saveCustomer(customer);
        pet.setOwner(customer);
        pet.setBehaviors(new HashSet<>());
        assertDoesNotThrow(() -> petService.savePet(pet));
        petService.deletePet(pet.getId());
        customerService.deleteCustomer(customer.getId());
    }

    @Test
    public void petWithoutOwnerGivesBadRequest() {
        PetDTO petDTO = new PetDTO();
        petDTO.setName("Coiso");
        petDTO.setNotes("cenas");
        petDTO.setType(PetType.CAT);
        petDTO.setBirthDate(LocalDate.now());
        String url = "http://localhost:" + port + "/pet";
        ResponseEntity<PetDTO> responseEntity = restTemplate.postForEntity(url, petDTO, PetDTO.class);
        //owner not found
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
