package org.k8s.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.k8s.poc.domain.Department;
import org.k8s.poc.domain.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HRService {

    @Inject
    @RestClient
    EmployeeService employeeService;

    @Inject
    @RestClient
    DepartmentService departmentService;


    public Uni<Boolean> assignEmployeeToDept(Employee employee) { return null; }

    public Uni<Boolean> unassignEmployeeToDept(Long id) { return null; }

}
