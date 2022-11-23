package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.CustomerInvalidUpdateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return petToPetDto(petService.savePet(petDtoToPet(petDTO)));
    }

    @PutMapping
    public void updatePet(@RequestBody PetDTO petDTO) {
        if (!(petDTO.getId() > 0)) {
            throw new PetInvalidUpdateException();
        }
        Pet pet = petService.getPet(petDTO.getId());
        if (!Objects.equals(petDTO.getName(), pet.getName())) {
            throw new PetInvalidUpdateException();
        }
        if (!Objects.equals(petDTO.getType(), pet.getType())) {
            throw new PetInvalidUpdateException();
        }
        if (petDTO.getBirthDate().compareTo(pet.getBirthDate()) != 0) {
            throw new PetInvalidUpdateException();
        }
        petService.savePet(petDtoToPet(petDTO));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return petToPetDto(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getPets().stream().map(this::petToPetDto).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.getPetsByOwner(ownerId).stream().map(this::petToPetDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable long petId) {
        petService.deletePet(petId);
    }

    private Pet petDtoToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setOwner(customerService.getCustomer(petDTO.getOwnerId()));
        return pet;
    }

    private PetDTO petToPetDto(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }
}
