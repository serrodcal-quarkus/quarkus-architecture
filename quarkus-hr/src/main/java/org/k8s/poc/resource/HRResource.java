package org.k8s.poc.resource;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countAssignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksAssignEmployeeToDept", description = "A measure of how much time takes to create a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> assignEmployeeToDept(@PathParam("employeeId") Long employeeId, @PathParam("deptId") Long deptId) {
        logger.info("assignEmployeeToDept with [employeeId:" + employeeId.toString() + ", dept:" + deptId.toString() + "]");
        try {
            return hrService.assignEmployeeToDept(employeeId, deptId).map(assigned -> {
                if (assigned) {
                    return Response.Status.OK;
                } else {
                    logger.error("Some error saving employee with [id:" + String.valueOf(employeeId) + "]");
                    return Response.Status.ACCEPTED;
                }
            }).map(status -> Response.status(status).build());
        } catch (RuntimeException e) {
            logger.error("Circuit breaker open in assignEmployeeToDept");
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Circuit breaker open")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
            return Uni.createFrom().item(response);
        }
    }

    @PUT
    @Path("/employee/{employeeId}/assign/{deptId}")
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countChangeDeptOfEmployee", description = "Count number of served messages")
    @Timed(name = "checksChangeDeptOfEmployee", description = "A measure of how much time takes to update a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> changeDeptOfEmployee(@PathParam("employeeId") Long employeeId, @PathParam("deptId") Long deptId) {
        logger.info("changeDeptOfEmployee with [name:" + employeeId.toString() + ", dept:" + deptId.toString() + "]");
        try {
            return hrService.assignEmployeeToDept(employeeId, deptId).map(assigned -> {
                if (assigned) {
                    return Response.Status.OK;
                } else {
                    logger.error("Some error saving employee with [id:" + String.valueOf(employeeId) + "]");
                    return Response.Status.ACCEPTED;
                }
            }).map(status -> Response.status(status).build());
        }  catch (RuntimeException e) {
            logger.error("Circuit breaker open in changeDeptOfEmployee");
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Circuit breaker open")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
            return Uni.createFrom().item(response);
        }
    }

    @DELETE
    @Path("/{id}")
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countUnassignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksUnassignEmployeeToDept", description = "A measure of how much time takes to delete a employee", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> unassignEmployeeToDept(@PathParam("id") Long id) {
        logger.info("unassignEmployeeToDept wit [id:" + id.toString() + "]");
        try {
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
        } catch (RuntimeException e) {
            logger.error("Circuit breaker open in unassignEmployeeToDept");
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Circuit breaker open")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
            return Uni.createFrom().item(response);
        }
    }

}
