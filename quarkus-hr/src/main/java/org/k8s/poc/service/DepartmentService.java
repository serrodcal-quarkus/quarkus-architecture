package org.k8s.poc.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.k8s.poc.domain.Department;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;

@Path("/department")
@RegisterRestClient //(configKey="department-api")
public interface DepartmentService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Department> getDepartments();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Department> getDepartment(@PathParam("id") Long id);

}
