# Quarkus Architecture

![](/img/quarkus.png)

Sample Quarkus architecture with the following dependencies:

* [Microprofile metrics](https://quarkus.io/guides/microprofile-metrics)
* [Microprofile health](https://quarkus.io/guides/microprofile-health)
* [Opentracing](https://quarkus.io/guides/opentracing)
* [Open API and Swagger](https://quarkus.io/guides/openapi-swaggerui)
* [GELF with ELK](https://quarkus.io/guides/centralized-log-management)


## Creating Kubernetes local cluster

Create a Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-config.yaml
```

## Deploying all the stack

Deploy the application with:
```
kubectl apply -f k8s
```

## Test

In order to test all the endpoints, please expose the API Gateway with:
```
kubectl port-forward <gateway_pod_name> 8080
```

Once the gateway is exposed open to the front application in a browser by [localhost:8080](http://localhost:8080).

![](/img/front.png)

Or use the browser following the pattern below:

* `localhost:8080/api/v1/{employee|department|hr}`

**Note**: Expose each microservice and access to `/swagger-ui` to read the API specification.

### HR example

Assign employee to a department:
```
curl -X POST localhost:8080/api/v1/hr/employee/2/assign/1
```

### Employee example

Get all employees:
```
curl localhost:8080/api/v1/employee
```

### Department example

Get all departments:
```
curl localhost:8080/api/v1/department
```

## Exposing dashboard

Expose the prometheus' dashboard with:
```
kubectl port-forward <prometheus_pod_name> 9090
```

Expose the grafana's dashboard with:
```
kubectl port-forward <grafana_pod_name> 3000
```

Expose the kibana's dashboard with:
```
kubectl port-forward <kibana_pod_name> 5601
```

Expose the jaeger's dashboard with:
```
kubectl port-forward <jaeger_pod_name> 16686
```

### Dashboards

Access to prometheus' dashboard with: [localhost:9090](http:/localhost:9090)

Access to grafana's dashboard with: [localhost:3000](http:/localhost:3000)

Access to kibana's dashboard with: [localhost:5601](http://localhost:5601)

Access to jaeger's dashboard with: [localhost:16686](http://localhost:16686)

## Load images to avoid errors

The first time all the services are deployed in Kubernetes, the nodes has to
download all the images in YAML files to the internal Docker's Registry. This may
cause some error at the first time. Try to load all the images to the nodes from
your local registry with:
```
kind load docker-image docker.elastic.co/elasticsearch/elasticsearch-oss:6.8.2 && \
kind load docker-image docker.elastic.co/logstash/logstash-oss:6.8.2 && \
kind load docker-image docker.elastic.co/kibana/kibana-oss:6.8.2 && \
kind load docker-image serrodcal/employee:1.0.0 && \
kind load docker-image serrodcal/department:1.0.0 && \
kind load docker-image serrodcal/hr:1.0.0 && \
kind load docker-image postgres:10.5 && \
kind load docker-image prom/prometheus:v2.17.1 && \
kind load docker-image grafana/grafana:6.7.2 && \
kind load docker-image jaegertracing/all-in-one:1.17.1
```

## Author

* [Serrodcal](https://github.com/serrodcal)
* [Lechowsky](https://github.com/lechowsky)
