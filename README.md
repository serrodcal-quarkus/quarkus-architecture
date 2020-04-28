# Quarkus Architecture

![](/img/quarkus.png)

Sample Quarkus architecture with the following dependencies:

* [Microprofile metrics](https://quarkus.io/guides/microprofile-metrics)
* [Microprofile health](https://quarkus.io/guides/microprofile-health)
* [Opentracing](https://quarkus.io/guides/opentracing)
* [Open API and Swagger](https://quarkus.io/guides/openapi-swaggerui)
* [GELF with ELK](https://quarkus.io/guides/centralized-log-management)
* [Fault tolerance](https://quarkus.io/guides/microprofile-fault-tolerance)

## Logical view

![](/img/logical.png)

## Physical view

![](/img/physical.png)

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

## Access to API Gateway

All the resources all exposed through NGINX server, which acts as an API Gateway:

```
kubectl port-forward <gateway_pod_name> 8080
```

Then open the following URL [localhost:8080](http://localhost:8080) in a browser
to access to the Web application.

![](/img/front.png)

### Accessing to the dashboards

Open the following URLs in a browser:

* Access to [grafana's dashboard](http://localhost:8080/grafana) with: `http://localhost:8080/grafana`

* Access to [kibana's dashboard](http://localhost:8080/kibana) with: `http://localhost:8080/kibana`

* Access to [jaeger's dashboard](http://localhost:8080/jaeger) with: `http://localhost:8080/jaeger`

* Access to [prometheus' dashboard](http://localhost:8080/prometheus) with: `http://localhost:8080/prometheus`

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
