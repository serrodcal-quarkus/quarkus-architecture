package org.k8s.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.k8s.poc.domain.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/employee")
@RegisterRestClient(configKey="employee-api")
public interface EmployeeService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Employee> getEmployees();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Employee> getEmployee(@PathParam("id") Long id);

    @GET
    @Path("/department/{deptId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Employee> getEmployeesByDept(@PathParam("deptId") Long deptId);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> updateEmployee(Employee employee);

}
