package org.serrodcal.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.serrodcal.poc.domain.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/employee")
@RegisterRestClient(configKey="employee-api")
public interface EmployeeService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Set<Employee>> getEmployees();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Employee> getEmployee(@PathParam("id") Long id);

    @GET
    @Path("/department/{deptId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Set<Employee>> getEmployeesByDept(@PathParam("deptId") Long deptId);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updateEmployee(Employee employee);

    @POST
    @Path("department/{deptId}/unassign")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> unassignEmployees(@PathParam("deptId") Long deptId);

}
