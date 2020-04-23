package org.k8s.poc.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.k8s.poc.domain.Department;
import org.k8s.poc.service.DepartmentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/api/v1/department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    private static final Logger logger = Logger.getLogger(DepartmentResource.class);

    private DepartmentService departmentService;

    DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GET
    @Counted(name = "countGetDepartments", description = "Count number of served messages")
    @Timed(name = "checksGetDepartments", description = "A measure of how much time takes to serve departments", unit = MetricUnits.MILLISECONDS)
    public Multi<Department> getDepartments() {
        logger.info("getDepartments");
        return departmentService.getDepartments();
    }

    @GET
    @Path("/{id}")
    @Counted(name = "countGetDepartment", description = "Count number of served messages")
    @Timed(name = "checksGetDepartment", description = "A measure of how much time takes to serve a department", unit = MetricUnits.MILLISECONDS)
    public Uni<Department> getDepartment(@PathParam("id") Long id) {
        logger.info("getDepartment with [id:" + id.toString() + "]");
        return departmentService.getDepartment(id);
    }

    @POST
    @Counted(name = "countCreateDepartment", description = "Count number of served messages")
    @Timed(name = "checksCreateDepartment", description = "A measure of how much time takes to create a department", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> createDepartment(Department department) {
        logger.info("createDepartment with [name:" + department.name + "]");
        return departmentService.createDepartment(department)
                .map(id -> {
                    if (Objects.nonNull(id)) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error saving department with [id:" + String.valueOf(id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

    @PUT
    @Counted(name = "countUpdateDepartment", description = "Count number of served messages")
    @Timed(name = "checksUpdateDepartment", description = "A measure of how much time takes to update a department", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> updateDepartment(Department department) {
        logger.info("updateDepartment with [name:" + department.name + "]");
        return departmentService.updateDepartment(department)
                .map(updated -> {
                    if (updated) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error updating department  with [id:" + String.valueOf(department.id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

    @DELETE
    @Path("/{id}")
    @Counted(name = "countDeleteDepartment", description = "Count number of served messages")
    @Timed(name = "checksDeleteDepartment", description = "A measure of how much time takes to delete a department", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> deleteDepartment(@PathParam("id") Long id) {
        logger.info("deleteDepartment wit [id:" + id.toString() + "]");
        return departmentService.deleteDepartment(id)
                .map(deleted -> {
                    if (deleted) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error deleting department  with [id:" + String.valueOf(id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

}
