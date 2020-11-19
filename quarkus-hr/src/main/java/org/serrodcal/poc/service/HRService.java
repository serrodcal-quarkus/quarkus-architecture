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

        return this.departmentService.getDepartment(deptId).flatMap(dept -> {
            return this.employeeService.getEmployee(employeeId).flatMap(empl -> {
                if (empl.deptId != dept.id) {
                    empl.deptId = dept.id;
                    return this.employeeService.updateEmployee(empl).flatMap(result -> {
                        if (result.getStatus() == 200) {
                            return Uni.createFrom().item(true);
                        } else {
                            return Uni.createFrom().item(false);
                        }
                    });
                } else {
                    return Uni.createFrom().item(false);
                }
            });
        });
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> unassignEmployee(Long id) {
        return this.employeeService.getEmployee(id).flatMap(empl -> {
            empl.deptId = null;
            return this.employeeService.updateEmployee(empl).flatMap(result -> {
               if (result.getStatus() == 200) {
                    return Uni.createFrom().item(true);
               } else {
                    return Uni.createFrom().item(false);
               }
           });
        });
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> unassignEmployees(Long deptId) {
        return this.departmentService.getDepartment(deptId).flatMap(dept -> {
            return this.employeeService.getEmployeesByDept(dept.id).flatMap(employees ->{
                if (employees.size() > 0){
                    return this.employeeService.unassignEmployees(deptId).flatMap(result -> {
                        if (result.getStatus() == 200) {
                            return Uni.createFrom().item(true);
                        } else {
                            return Uni.createFrom().item(false);
                        }
                    });
                } else {
                    return Uni.createFrom().item(false);
                }
            });
        });
    }

}
