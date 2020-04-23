package org.k8s.poc.repository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.k8s.poc.dao.EmployeeDao;
import org.k8s.poc.domain.Employee;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeRepository {

    private EmployeeDao employeeDao;

    EmployeeRepository(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public Multi<Employee> getEmployees() {
        return employeeDao.findAll();
    }

    public Uni<Employee> getEmployee(Long id) {
        return employeeDao.findById(id);
    }

    public Uni<Long> saveEmployee(Employee employee) { return employeeDao.save(employee.name); }

    public Uni<Boolean> updateEmployee(Employee employee) { return employeeDao.update(employee.id, employee.name); }

    public Uni<Boolean> deleteEmployee(Long id) { return employeeDao.delete(id); }
}
