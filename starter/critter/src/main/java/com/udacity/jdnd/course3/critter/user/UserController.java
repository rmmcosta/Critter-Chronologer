package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerToCustomerDTO(customerService.saveCustomer(customerDtoToCustomer(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getCustomers().stream().map(this::customerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        return customerToCustomerDTO(customerService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeToEmployeeDTO(employeeService.saveEmployee(employeeDtoToEmployee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return employeeToEmployeeDTO(employeeService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return employeeService.getEmployeesForService(employeeDTO.getDate(), employeeDTO.getSkills())
                .stream()
                .map(this::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    private Customer customerDtoToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    private CustomerDTO customerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> pets = petService.getPetsByOwner(customer.getId());
        if (pets != null)
            customerDTO.setPetIds(pets.stream().map(Pet::getId).collect(Collectors.toList()));
        return customerDTO;
    }

    private Employee employeeDtoToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setAvailability(employeeDTO.getDaysAvailable());
        return employee;
    }

    private EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setDaysAvailable(employee.getAvailability());
        return employeeDTO;
    }

}
