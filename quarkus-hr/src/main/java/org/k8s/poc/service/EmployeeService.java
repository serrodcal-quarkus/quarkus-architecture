package org.k8s.poc.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Path;

@Path("/employee")
@RegisterRestClient(configKey="employee-api")
public interface EmployeeService {
}
