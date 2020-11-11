package org.serrodcal.poc.domain;

public class Employee {

    public Long id;
    public String name;
    public Long deptId;

    public Employee() {}

    public Employee(String name, Long deptId) {
        this.name = name;
        this.deptId = deptId;
    }

    public Employee(Long id, String name, Long deptId){
        this.id = id;
        this.name = name;
        this.deptId = deptId;
    }

}
