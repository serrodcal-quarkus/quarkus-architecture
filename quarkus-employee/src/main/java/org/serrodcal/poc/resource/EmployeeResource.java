package org.serrodcal.poc.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Body;
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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.serrodcal.poc.domain.Employee;
import org.serrodcal.poc.service.EmployeeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@RouteBase(path = "api/v2")
@Tag(name = "Employee Resource", description = "Exposing Employee API to manage employees from the company using Vert.x")
public class EmployeeResource {

    private static final Logger logger = Logger.getLogger(EmployeeResource.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String TEXT_PLAIN = "text/plain";

    @Inject
    EmployeeService employeeService;

    @Route(path = "employee", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get list of employees",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.ARRAY)))
    @APIResponse(responseCode="204",
            description="No employees",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countGetEmployees", description = "Count number of served messages")
    @Timed(name = "checksGetEmployees", description = "A measure of how much time takes to serve employees", unit = MetricUnits.MILLISECONDS)
    void getEmployees(RoutingContext rc) {
        logger.info("getEmployees");
        this.employeeService.getEmployees().collectItems().asList().subscribe().with(result -> {
                if (result != null) {
                    rc.response()
                      .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                      .setStatusCode(HttpResponseStatus.OK.code())
                      .end(Json.encode(result));
                } else {
                    rc.response()
                      .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                      .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                      .end("No employees");
                }
            },
            failure -> {
                rc.response()
                  .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .end(failure.getMessage());
            });
    }


    @Route(path = "employee/:id", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get employee",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="204",
            description="No employee",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countGetEmployee", description = "Count number of served messages")
    @Timed(name = "checksGetEmployee", description = "A measure of how much time takes to serve a employee", unit = MetricUnits.MILLISECONDS)
    void getEmployee(RoutingContext rc, @Param("id") String id) {
        logger.info("getEmployee with [id:" + id.toString() + "]");
        this.employeeService.getEmployee(Long.valueOf(id)).subscribe().with(result -> {
                if (result != null) {
                    rc.response()
                       .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                       .setStatusCode(HttpResponseStatus.OK.code())
                       .end(Json.encode(result));
                } else {
                    rc.response()
                       .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                       .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                       .end("No employees");
                }
            },
            failure -> {
                rc.response()
                  .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .end(failure.getMessage());
            });
    }

    @Route(path = "employee/department/:deptId", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get list of employees by department",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.ARRAY)))
    @APIResponse(responseCode="204",
            description="No employees",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countGetEmployeesByDept", description = "Count number of served messages")
    @Timed(name = "checksGetEmployeesByDept", description = "A measure of how much time takes to serve employees", unit = MetricUnits.MILLISECONDS)
    void getEmployeesByDept(RoutingContext rc, @Param("deptId") String deptId) {
        logger.info("getEmployeesByDept with [deptId: " + deptId.toString() + "]");
        this.employeeService.getEmployeesByDept(Long.valueOf(deptId)).collectItems().asList().subscribe().with(result -> {
                if (result != null)
                    rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(Json.encode(result));
                else
                    rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                        .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                        .end("No employees");
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }

    @Route(path = "employee", methods = HttpMethod.POST)
    @RequestBody(required = true,
            content = @Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="200",
            description="Get the id of the new employee",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="500",
            description="Employee can not be created",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countCreateEmployee", description = "Count number of served messages")
    @Timed(name = "checksCreateEmployee", description = "A measure of how much time takes to create a employee", unit = MetricUnits.MILLISECONDS)
    void createEmployee(RoutingContext rc, @Body Employee employee) {
        logger.info("createEmployee with [name:" + employee.name + "]");
        this.employeeService.createEmployee(employee).subscribe().with(result -> {
            if (result != null) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                  .end("Employee do not added");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }

    @Route(path = "employee", methods = HttpMethod.PUT)
    @RequestBody(required = true,
            content = @Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="200",
            description="Get true or false if the employee has been update or not",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Employee can not be updated",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countUpdateEmployee", description = "Count number of served messages")
    @Timed(name = "checksUpdateEmployee", description = "A measure of how much time takes to update a employee", unit = MetricUnits.MILLISECONDS)
    void updateEmployee(RoutingContext rc, @Body Employee employee) {
        logger.info("updateEmployee with [name:" + employee.name + "]");
        this.employeeService.updateEmployee(employee).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not added");
            }
        },
        failure -> {
            rc.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                    .end(failure.getMessage());
        });
    }

    @Route(path = "employee/:id", methods = HttpMethod.DELETE)
    @APIResponse(responseCode="200",
            description="Selected employee was deleted",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Employee can not be deleted",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countDeleteEmployee", description = "Count number of served messages")
    @Timed(name = "checksDeleteEmployee", description = "A measure of how much time takes to delete a employee", unit = MetricUnits.MILLISECONDS)
    void deleteEmployee(RoutingContext rc, @Param("id") String id) {
        logger.info("deleteEmployee wit [id:" + id.toString() + "]");
        this.employeeService.deleteEmployee(Long.valueOf(id)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not added");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }

    @Route(path = "employee/department/:id/unassign", methods = HttpMethod.POST)
    void unassignedEmployees(RoutingContext rc, @Param String deptId) {
        logger.info("unassignedEmployees with [deptId:" + deptId + "]");
        this.employeeService.unassignEmployees(Long.valueOf(deptId)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Employee do not added");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }

}
