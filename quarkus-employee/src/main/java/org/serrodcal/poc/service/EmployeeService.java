package org.serrodcal.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.serrodcal.poc.domain.Employee;
import org.serrodcal.poc.repository.EmployeeRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Multi<Employee> getEmployees() { return employeeRepository.getEmployees(); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Employee> getEmployee(Long id) { return employeeRepository.getEmployee(id); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Multi<Employee> getEmployeesByDept(Long deptId) { return employeeRepository.getEmployeesByDept(deptId); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Long> createEmployee(Employee employee) { return employeeRepository.saveEmployee(employee); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> updateEmployee(Employee employee) { return employeeRepository.updateEmployee(employee); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> unassignEmployees(Long deptId) { return employeeRepository.unassignEmployees(deptId); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> deleteEmployee(Long id) { return employeeRepository.deleteEmployee(id); }

}
