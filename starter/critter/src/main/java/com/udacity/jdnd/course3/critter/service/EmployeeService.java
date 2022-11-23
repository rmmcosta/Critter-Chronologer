package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    public Employee getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
        if (employee.getAvailability() == null) {
            employee.setAvailability(employeeRepository.findAvailabilityById(id)
                    .stream()
                    .map(DayOfWeek::of)
                    .collect(Collectors.toSet()));
        }
        return employee;
    }

    @Transactional
    public void setAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        for (DayOfWeek dayOfWeek : daysAvailable) {
            employeeRepository.saveAvailability(employeeId, dayOfWeek.getValue());
        }
    }

    public List<Employee> getEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        return getEmployeesForServiceAlternative(date, skills);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private List<Employee> getEmployeesForServiceAlternative(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> employeesForService = new ArrayList<>();
        for (Employee employee : getEmployeesWithAvailabilityAndSkills()) {
            boolean hasAvailability = employee.getAvailability().stream().anyMatch(dayOfWeek -> dayOfWeek.compareTo(date.getDayOfWeek()) == 0);
            boolean hasSkills = skills
                    .stream()
                    .allMatch(employeeSkill -> employee.getSkills()
                            .stream()
                            .anyMatch(expectedEmployeeSkill -> expectedEmployeeSkill
                                    .compareTo(employeeSkill) == 0
                            )
                    );
            if (hasAvailability && hasSkills) {
                employeesForService.add(employee);
            }
        }
        return employeesForService;
    }

    private List<Employee> getEmployeesWithAvailabilityAndSkills() {
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        List<Employee> employeesWithAvailabilityAndSkills = new ArrayList<>();
        for (Employee employee : employees) {
            Employee employeeWithAvailabilityAndSkills = new Employee();
            BeanUtils.copyProperties(employee, employeeWithAvailabilityAndSkills);
            if (employeeWithAvailabilityAndSkills.getAvailability() == null) {
                Set<DayOfWeek> availability = employeeRepository.findAvailabilityById(employee.getId())
                        .stream()
                        .map(DayOfWeek::of)
                        .collect(Collectors.toSet());
                employeeWithAvailabilityAndSkills.setAvailability(availability);
            }
            if (employeeWithAvailabilityAndSkills.getSkills() == null) {
                Set<EmployeeSkill> skills = employeeRepository.findSkillsById(employee.getId())
                        .stream()
                        .map(integer -> EmployeeSkill.values()[integer])
                        .collect(Collectors.toSet());
                employeeWithAvailabilityAndSkills.setSkills(skills);
            }
            employeesWithAvailabilityAndSkills.add(employeeWithAvailabilityAndSkills);
        }
        return employeesWithAvailabilityAndSkills;
    }
}
