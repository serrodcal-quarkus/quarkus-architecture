# quarkus-prometheus

![](/img/quarkus.png)

Sample Quarkus application with the following dependencies:

* [Microprofile metrics](https://quarkus.io/guides/microprofile-metrics)
* [Microprofile health](https://quarkus.io/guides/microprofile-health)
* [Opentracing](https://quarkus.io/guides/opentracing)
* [Open API and Swagger](https://quarkus.io/guides/openapi-swaggerui)
* [GELF with ELK](https://quarkus.io/guides/centralized-log-management)

## Endpoints


## Creating Kubernetes local cluster

Create a Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-config.yaml
```

If you want to use Istio follow below instructions:
```
istioctl manifest apply --set profile=demo
```

Auto sidecar injection with:
```
kubectl label ns default istio-injection=enabled
```

**Note**: `istio-injection=enabled` label enable the automatic sidecar injection.
Remove label with: `kubectl label ns default istio-injection-`

Or, manual sidecar injection with:
```
cat k8s/<file>.yaml | istioctl kube-inject -f - | kubectl apply -f -
```

### Clean up Istio

Remove all Istio components with:
```
istioctl manifest generate --set profile=demo | kubectl delete -f -
```

## Deploying all the stack

Deploy the application with:
```
kubectl apply -f k8s
```

## Exposing ports

Expose the application with:
```
kubectl port-forward <hr_pod_name> 8080
kubectl port-forward <employee_pod_name> 8081:8080
kubectl port-forward <department_pod_name> 8082:8080
```

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

Access to kiali's Dashboard (`admin;admin`) provided by Istio (only if you enable Istio):
```
istioctl dashboard kiali
```

## Test

### HR

* Assign employee to a department:
```
curl -X POST localhost:8080/api/v1/hr/employee/2/assign/1
```

### Employee

* Get all employees:
```
curl localhost:8081/api/v1/employee
```

### Department

* Get all departments:
```
curl localhost:8081/api/v1/department
```

## Dashboards

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
