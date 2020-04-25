package org.k8s.poc.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Path;

@Path("/department")
@RegisterRestClient(configKey="department-api")
public interface DepartmentService {
}
