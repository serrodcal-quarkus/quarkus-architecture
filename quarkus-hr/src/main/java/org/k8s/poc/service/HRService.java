package org.k8s.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.k8s.poc.domain.Employee;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HRService {

    private EmployeeRepository employeeRepository;

    HRService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Multi<Employee> getEmployees() { return employeeRepository.getEmployees(); }

    public Uni<Employee> getEmployee(Long id) { return employeeRepository.getEmployee(id); }

    public Uni<Long> createEmployee(Employee employee) { return employeeRepository.saveEmployee(employee); }

    public Uni<Boolean> updateEmployee(Employee employee) { return employeeRepository.updateEmployee(employee); }

    public Uni<Boolean> deleteEmployee(Long id) { return employeeRepository.deleteEmployee(id); }

}
