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
import org.serrodcal.poc.domain.Department;
import org.serrodcal.poc.service.DepartmentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@RouteBase(path = "api/v2")
@Tag(name = "Department Resource", description = "Exposing Department API to manage departments from the company using Vert.x")
public class DepartmentResource {

    private static final Logger logger = Logger.getLogger(DepartmentResource.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String TEXT_PLAIN = "text/plain";

    @Inject
    DepartmentService departmentService;

    @Route(path = "department", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get list of department",
            content=@Content(mediaType="application/json", schema=@Schema(type= SchemaType.ARRAY)))
    @APIResponse(responseCode="204",
            description="No department",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countGetDepartments", description = "Count number of served messages")
    @Timed(name = "checksGetDepartments", description = "A measure of how much time takes to serve departments", unit = MetricUnits.MILLISECONDS)
    void getDepartments(RoutingContext rc) {
        logger.info("getDepartments");
        this.departmentService.getDepartments().collectItems().asList().subscribe().with(result -> {
            if (result != null) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                  .end("No departments");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }


    @Route(path = "department/:id", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get department",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="204",
            description="No department",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countGetDepartment", description = "Count number of served messages")
    @Timed(name = "checksGetDepartment", description = "A measure of how much time takes to serve a department", unit = MetricUnits.MILLISECONDS)
    void getDepartment(RoutingContext rc, @Param("id") String id) {
        logger.info("getDepartment with [id:" + id.toString() + "]");
        this.departmentService.getDepartment(Long.valueOf(id)).subscribe().with(result -> {
            if (result != null) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                  .end("No department");
            }
        },
        failure -> {
            rc.response()
              .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
              .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
              .end(failure.getMessage());
        });
    }

    @Route(path = "department", methods = HttpMethod.POST)
    @RequestBody(required = true,
            content = @Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="200",
            description="Get the id of the new department",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="500",
            description="Department can not be created",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countCreateDepartment", description = "Count number of served messages")
    @Timed(name = "checksCreateDepartment", description = "A measure of how much time takes to create a department", unit = MetricUnits.MILLISECONDS)
    void createDepartment(RoutingContext rc, @Body Department department) {
        logger.info("createDepartment with [name:" + department.name + "]");
        this.departmentService.createDepartment(department).subscribe().with(result -> {
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

    @Route(path = "department", methods = HttpMethod.PUT)
    @RequestBody(required = true,
            content = @Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT)))
    @APIResponse(responseCode="200",
            description="Get true or false if the department has been update or not",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Department can not be updated",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countUpdateDepartment", description = "Count number of served messages")
    @Timed(name = "checksUpdateDepartment", description = "A measure of how much time takes to update a department", unit = MetricUnits.MILLISECONDS)
    void updateDepartment(RoutingContext rc, @Body Department department) {
        logger.info("updateDepartment with [name:" + department.name + "]");
        this.departmentService.updateDepartment(department).subscribe().with(result -> {
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

    @Route(path = "department/:id", methods = HttpMethod.DELETE)
    @APIResponse(responseCode="200",
            description="Selected department was deleted",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="202",
            description="Department can not be deleted",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING)))
    @Timeout(1000)
    @Retry(maxRetries = 4)
    @Counted(name = "countDeleteDepartment", description = "Count number of served messages")
    @Timed(name = "checksDeleteDepartment", description = "A measure of how much time takes to delete a department", unit = MetricUnits.MILLISECONDS)
    void deleteDepartment(RoutingContext rc, @Param("id") String id) {
        logger.info("deleteDepartment wit [id:" + id.toString() + "]");
        this.departmentService.deleteDepartment(Long.valueOf(id)).subscribe().with(result -> {
            if (result) {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.OK.code())
                  .end(Json.encode(result.toString()));
            } else {
                rc.response()
                  .putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
                  .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                  .end("Department do not added");
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
