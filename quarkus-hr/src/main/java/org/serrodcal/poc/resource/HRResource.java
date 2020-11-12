package org.serrodcal.poc.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.serrodcal.poc.service.HRService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@RouteBase(path = "api/v2")
@Tag(name = "HR Resource", description = "Exposing HR API to manage employees and departments from the company using Vert.x")
public class HRResource {

    private static final Logger logger = Logger.getLogger(HRResource.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String TEXT_PLAIN = "text/plain";

    @Inject
    HRService hrService;

    @Route(path = "hr/employee/:employeeId/assign/:deptId", methods = HttpMethod.POST)
    @APIResponse(responseCode="200",
            description="Get true if the employee has been assigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type= SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Employee can not be assigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countAssignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksAssignEmployeeToDept", description = "A measure of how much time takes to assign a employee to a given Department", unit = MetricUnits.MILLISECONDS)
    void assignEmployeeToDept(RoutingContext rc, @Param("employeeId") String employeeId, @Param("deptId") String deptId) {
        logger.info("assignEmployeeToDept with [employeeId:" + employeeId + ", dept:" + deptId + "]");
        this.hrService.assignEmployeeToDept(Long.valueOf(employeeId), Long.valueOf(deptId)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not assigned");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }



    @Route(path = "hr/employee/:employeeId/assign/:deptId", methods = HttpMethod.PUT)
    @APIResponse(responseCode="200",
            description="Get true if the employee has been unassigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type= SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Employee can not be unassigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countChangeDeptOfEmployee", description = "Count number of served messages")
    @Timed(name = "checksChangeDeptOfEmployee", description = "A measure of how much time takes to change a employee to a given Department", unit = MetricUnits.MILLISECONDS)
    void changeDeptOfEmployee(RoutingContext rc, @Param("employeeId") String employeeId, @Param("deptId") String deptId) {
        logger.info("changeDeptOfEmployee with [name:" + employeeId + ", dept:" + deptId + "]");
        this.hrService.assignEmployeeToDept(Long.valueOf(employeeId), Long.valueOf(deptId)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not assigned");
            }
        },
        failure -> {
            rc.response()
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .end(failure.getMessage());
        });
    }

    @Route(path = "hr/:id", methods = HttpMethod.DELETE)
    @APIResponse(responseCode="200",
            description="Get true if the employee has been unassigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type= SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Employee can not be unassigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countUnassignEmployeeToDept", description = "Count number of served messages")
    @Timed(name = "checksUnassignEmployeeToDept", description = "A measure of how much time takes to unassign a employee to a given Department", unit = MetricUnits.MILLISECONDS)
    void unassignEmployeeToDept(RoutingContext rc, @Param("id") String id) {
        logger.info("unassignEmployeeToDept wit [id:" + id + "]");
        this.hrService.unassignEmployeeToDept(Long.valueOf(id)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not unassigned");
            }
        },
        failure -> {
            rc.response()
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .end(failure.getMessage());
        });
    }

    @Route(path = "hr/department/:deptId", methods = HttpMethod.DELETE)
    @APIResponse(responseCode="200",
            description="Get true if the department has been deleted and all the employees who belong to it have also been unassigned",
            content=@Content(mediaType="text/plain", schema=@Schema(type= SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Department can not be deleted",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countDeleteDepartment", description = "Count number of served messages")
    @Timed(name = "checksDeleteDepartment", description = "A measure of how much time takes to delete a department and also unassign all the employees who belong to it", unit = MetricUnits.MILLISECONDS)
    void deleteDepartment(RoutingContext rc, @Param("deptId") String deptId) {
        this.hrService.deleteDepartment(Long.valueOf(deptId)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Department can not be deleted");
            }
        },
        failure -> {
            rc.response()
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .end(failure.getMessage());
        });
    }

}
