package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Modifying
    @Query(
            value = "insert into employee_availability values(:employeeId, :dayAvailable)",
            nativeQuery = true
    )
    void saveAvailability(Long employeeId, int dayAvailable);

    List<Employee> findEmployeesByAvailabilityAndSkillsIn(DayOfWeek availability, Set<EmployeeSkill> skillSet);

    @Query(value = "Select availability from employee_availability where employee_id=:id", nativeQuery = true)
    List<Integer> findAvailabilityById(Long id);

    @Query(value = "Select skill from employee_skill where employee_id=:id", nativeQuery = true)
    List<Integer> findSkillsById(Long id);

    @Query(
            value = "Select e.* " +
                    "from employee e " +
                    "where not exists ( select 1 " +
                                        "from schedule_employee se " +
                                        "inner join schedule s " +
                                        "on s.id = se.schedule_id " +
                                        "where se.employee_id = e.id " +
                                        "and s.schedule_date = :date " +
                                        "and (  :startTime between s.start_time and s.end_time " +
                                                "or :endTime between s.start_time and s.end_time" +
                                        ")" +
                    ")",
            nativeQuery = true
    )
    List<Employee> findAvailableEmployeesByTimeslot(LocalTime startTime, LocalTime endTime, LocalDate date);
}
