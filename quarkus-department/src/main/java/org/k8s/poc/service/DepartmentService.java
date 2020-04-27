package org.k8s.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.k8s.poc.domain.Department;
import org.k8s.poc.repository.DepartmentRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentService {

    private DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Multi<Department> getDepartments() { return departmentRepository.getDepartments(); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Department> getDepartment(Long id) { return departmentRepository.getDepartment(id); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Long> createDepartment(Department department) { return departmentRepository.saveDepartment(department); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> updateDepartment(Department department) { return departmentRepository.updateDepartment(department); }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Boolean> deleteDepartment(Long id) { return departmentRepository.deleteDepartment(id); }

}
