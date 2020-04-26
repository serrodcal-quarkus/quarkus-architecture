package org.k8s.poc.service;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.k8s.poc.domain.Department;
import org.k8s.poc.domain.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class HRService {

    @Inject
    @RestClient
    EmployeeService employeeService;

    @Inject
    @RestClient
    DepartmentService departmentService;

    public Uni<Boolean> assignEmployeeToDept(Long employeeId, Long deptId) {
        return departmentService.getDepartment(deptId).flatMap(dept -> {
            if (Objects.nonNull(dept)){
                return employeeService.getEmployee(employeeId).flatMap( empl -> {
                    if (Objects.nonNull(empl) && empl.deptId != dept.id){
                        empl.deptId = dept.id;
                        return employeeService.updateEmployee(empl);
                    } else {
                        return Uni.createFrom().item(false);
                    }
                } );
            } else {
                return Uni.createFrom().item(false);
            }
        });
    }

    public Uni<Boolean> unassignEmployeeToDept(Long id) {
        return employeeService.getEmployee(id).flatMap( empl -> {
            if (Objects.nonNull(empl)) {
                empl.deptId = null;
                return employeeService.updateEmployee(empl);
            } else {
                return Uni.createFrom().item(false);
            }
        });
    }

}
