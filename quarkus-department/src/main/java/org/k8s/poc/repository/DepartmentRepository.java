package org.k8s.poc.repository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.k8s.poc.dao.DepartmentDao;
import org.k8s.poc.domain.Department;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepository {

    private DepartmentDao departmentDao;

    DepartmentRepository(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public Multi<Department> getDepartments() {
        return departmentDao.findAll();
    }

    public Uni<Department> getDepartment(Long id) {
        return departmentDao.findById(id);
    }

    public Uni<Long> saveDepartment(Department department) { return departmentDao.save(department.name); }

    public Uni<Boolean> updateDepartment(Department department) { return departmentDao.update(department.id, department.name); }

    public Uni<Boolean> deleteDepartment(Long id) { return departmentDao.delete(id); }
}
