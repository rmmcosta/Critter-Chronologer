package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findCustomerByPets_Id(Long id);
}
