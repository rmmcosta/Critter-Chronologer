package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.Behavior;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetInvalidException;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.service.BehaviorService;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.WrongBehaviorException;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetBehaviorTests {
    @Autowired
    BehaviorService behaviorService;
    @Autowired
    CustomerService customerService;
    @Autowired
    PetService petService;

    @Test
    public void correctBehaviorsForPetTypeAreReturned() {
        int initialCount = behaviorService.getAllBehaviorsByPetType(PetType.CAT).size();
        Behavior behavior = new Behavior();
        behavior.setName("Chase mouses");
        behavior.setDescription("The behavior of chasing mouses is typical of cats.");
        behavior.setPetTypes(List.of(PetType.CAT));
        final Long behaviorId = behaviorService.saveBehavior(behavior).getId();
        List<Behavior> catBehaviors = behaviorService.getAllBehaviorsByPetType(PetType.CAT);
        assertEquals(initialCount + 1, catBehaviors.size());
        assertTrue(catBehaviors.stream().anyMatch(behavior1 -> behavior1.getId().equals(behaviorId)));
    }

    @Test
    public void associatingBehaviorFromAnotherPetThrowsException() {
        Behavior behavior = new Behavior();
        behavior.setName("Chase bones");
        behavior.setDescription("The behavior of chasing bones is typical of dogs.");
        behavior.setPetTypes(List.of(PetType.DOG));
        behavior = behaviorService.saveBehavior(behavior);

        Pet pet = new Pet();
        pet.setName("Xuxas");
        pet.setBirthDate(LocalDate.now());
        pet.setNotes("coiso et al");
        pet.setType(PetType.CAT);
        Customer customer = new Customer();
        customer.setName("Customer 1");
        customer.setNotes("xpto");
        customer.setPhoneNumber("1234-556");
        customer = customerService.saveCustomer(customer);
        pet.setOwner(customer);
        pet.setBehaviors(Set.of(behavior));
        assertThrows(WrongBehaviorException.class, () -> petService.savePet(pet), "Wrong behavior for this type of pet");
    }

    @Test
    public void associatingBehaviorFromCorrectPetWorksOk() {
        Behavior behavior = new Behavior();
        behavior.setName("Chase bones");
        behavior.setDescription("The behavior of chasing bones is typical of dogs.");
        behavior.setPetTypes(List.of(PetType.DOG));
        behavior = behaviorService.saveBehavior(behavior);

        Pet pet = new Pet();
        pet.setName("Auau");
        pet.setBirthDate(LocalDate.now());
        pet.setNotes("coiso et al");
        pet.setType(PetType.DOG);
        Customer customer = new Customer();
        customer.setName("Customer 1");
        customer.setNotes("xpto");
        customer.setPhoneNumber("1234-556");
        customer = customerService.saveCustomer(customer);
        pet.setOwner(customer);
        pet.setBehaviors(Set.of(behavior));
        assertDoesNotThrow(() -> petService.savePet(pet));
    }
}
