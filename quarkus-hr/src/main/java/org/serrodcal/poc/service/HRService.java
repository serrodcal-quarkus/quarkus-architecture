package org.serrodcal.poc.service;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> assignEmployeeToDept(Long employeeId, Long deptId) {
        return departmentService.getDepartment(deptId).flatMap(dept -> {
            return employeeService.getEmployee(employeeId).flatMap( empl -> {
                if (empl.deptId != dept.id){
                    empl.deptId = dept.id;
                    return employeeService.updateEmployee(empl).flatMap(result -> {
                        return Uni.createFrom().item(result);
                    });
                } else {
                    return Uni.createFrom().item(false);
                }
            } );
        });
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> unassignEmployeeToDept(Long id) {
        return this.employeeService.getEmployee(id).flatMap(empl -> {
           return this.employeeService.updateEmployee(empl).flatMap(result -> {
               return Uni.createFrom().item(result);
           });
        });
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> deleteDepartment(Long deptId) {
        return this.departmentService.getDepartment(deptId).flatMap(dept -> {
            return this.employeeService.unassignEmployees(deptId).flatMap(result -> {
                return Uni.createFrom().item(result);
            });
        });
    }

}
