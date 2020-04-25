package org.k8s.poc.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.k8s.poc.domain.Employee;
import org.k8s.poc.service.HRService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/api/v1/hr")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HRResource {

    private static final Logger logger = Logger.getLogger(HRResource.class);

    private HRService HRService;

    HRResource(HRService HRService) {
        this.HRService = HRService;
    }

    @GET
    @Counted(name = "countGetEmployees", description = "Count number of served messages")
    @Timed(name = "checksGetEmployees", description = "A measure of how much time takes to serve employees", unit = MetricUnits.MILLISECONDS)
    public Multi<Employee> getEmployees() {
        logger.info("getEmployees");
        return HRService.getEmployees();
    }

    @GET
    @Path("/{id}")
    @Counted(name = "countGetEmployee", description = "Count number of served messages")
    @Timed(name = "checksGetEmployee", description = "A measure of how much time takes to serve a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Employee> getEmployee(@PathParam("id") Long id) {
        logger.info("getEmployee with [id:" + id.toString() + "]");
        return HRService.getEmployee(id);
    }

    @POST
    @Counted(name = "countCreateEmployee", description = "Count number of served messages")
    @Timed(name = "checksCreateEmployee", description = "A measure of how much time takes to create a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> createEmployee(Employee employee) {
        logger.info("createEmployee with [name:" + employee.name + "]");
        return HRService.createEmployee(employee)
                .map(id -> {
                    if (Objects.nonNull(id)) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error saving employee with [id:" + String.valueOf(id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

    @PUT
    @Counted(name = "countUpdateEmployee", description = "Count number of served messages")
    @Timed(name = "checksUpdateEmployee", description = "A measure of how much time takes to update a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> updateEmployee(Employee employee) {
        logger.info("updateEmployee with [name:" + employee.name + "]");
        return HRService.updateEmployee(employee)
                .map(updated -> {
                    if (updated) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error updating employee  with [id:" + String.valueOf(employee.id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

    @DELETE
    @Path("/{id}")
    @Counted(name = "countDeleteEmployee", description = "Count number of served messages")
    @Timed(name = "checksDeleteEmployee", description = "A measure of how much time takes to delete a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> deleteEmployee(@PathParam("id") Long id) {
        logger.info("deleteEmployee wit [id:" + id.toString() + "]");
        return HRService.deleteEmployee(id)
                .map(deleted -> {
                    if (deleted) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error deleting employee  with [id:" + String.valueOf(id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

}
