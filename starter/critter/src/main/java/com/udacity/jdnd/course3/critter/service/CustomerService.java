package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PetService petService;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public List<Customer> getCustomersWithPets() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        List<Customer> customersWithPets = new ArrayList<>();
        Customer customerWithPet = new Customer();
        for (Customer customer : customers) {
            BeanUtils.copyProperties(customer, customerWithPet);
            customerWithPet.setPets(new HashSet<>(petService.getPetsByOwner(customer.getId())));
            customersWithPets.add(customerWithPet);
        }
        return customersWithPets;
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    public Customer getOwnerByPet(Long petId) {
        return customerRepository.findCustomerByPets_Id(petId);
    }

    public Customer getOwnerWithPetsByPet(Long petId) {
        Customer customer = customerRepository.findCustomerByPets_Id(petId);
        Customer customerWithPets = new Customer();
        BeanUtils.copyProperties(customer, customerWithPets);
        customerWithPets.setPets(new HashSet<>(petService.getPetsByOwner(customerWithPets.getId())));
        return customerWithPets;
    }
}
