package org.k8s.poc.resource;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.k8s.poc.service.HRService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/hr")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HRResource {

    private static final Logger logger = Logger.getLogger(HRResource.class);

    private HRService hrService;

    HRResource(HRService HRService) {
        this.hrService = HRService;
    }

    @POST
    @Path("/employee/{employeeId}/assign/{deptId}")
    @Counted(name = "countAssignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksAssignEmployeeToDept", description = "A measure of how much time takes to create a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> assignEmployeeToDept(@PathParam("employeeId") Long employeeId, @PathParam("deptId") Long deptId) {
        logger.info("assignEmployeeToDept with [employeeId:" + employeeId.toString() + ", dept:" + deptId.toString() + "]");
        return hrService.assignEmployeeToDept(employeeId, deptId).map(assigned -> {
            if (assigned) {
                return Response.Status.OK;
            } else {
                logger.error("Some error saving employee with [id:" + String.valueOf(employeeId) + "]");
                return Response.Status.ACCEPTED;
            }
        }).map(status -> Response.status(status).build());
    }

    @PUT
    @Path("/employee/{employeeId}/assign/{deptId}")
    @Counted(name = "countChangeDeptOfEmployee", description = "Count number of served messages")
    @Timed(name = "checksChangeDeptOfEmployee", description = "A measure of how much time takes to update a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> changeDeptOfEmployee(@PathParam("employeeId") Long employeeId, @PathParam("deptId") Long deptId) {
        logger.info("changeDeptOfEmployee with [name:" + employeeId.toString() + ", dept:" + deptId.toString() + "]");
        return hrService.assignEmployeeToDept(employeeId, deptId).map(assigned -> {
            if (assigned) {
                return Response.Status.OK;
            } else {
                logger.error("Some error saving employee with [id:" + String.valueOf(employeeId) + "]");
                return Response.Status.ACCEPTED;
            }
        }).map(status -> Response.status(status).build());
    }

    @DELETE
    @Path("/{id}")
    @Counted(name = "countUnassignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksUnassignEmployeeToDept", description = "A measure of how much time takes to delete a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> unassignEmployeeToDept(@PathParam("id") Long id) {
        logger.info("unassignEmployeeToDept wit [id:" + id.toString() + "]");
        return hrService.unassignEmployeeToDept(id)
                .map(updated -> {
                    if (updated) {
                        return Response.Status.OK;
                    } else {
                        logger.error("Some error deleting employee  with [id:" + String.valueOf(id) + "]");
                        return Response.Status.ACCEPTED;
                    }
                })
                .map(status -> Response.status(status).build());
    }

}
