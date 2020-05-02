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
kind create cluster
```

**Note**: Use `--image kindest/node:<target_version>` if you want to choose different version of k8s.

### Enable Ingress

According to [this page](https://kind.sigs.k8s.io/docs/user/ingress/), it is possible
to create an ingress controller to assign an static IP to your cluster and also
to be able to call the componentes using an ingress and avoiding port forwarding.

![](/img/ingress.png)

In this case, the port forward command below it isn't necessary at all.

Just create the cluster with:
```
kind create cluster --config kind/kind-ingress-config.yaml
```

Then, deploy the Contour components:
```
kubectl apply -f https://projectcontour.io/quickstart/contour.yaml
```

Apply kind specific patches to forward the hostPorts to the ingress controller,
set taint tolerations and schedule it to the custom labelled node:
```
kubectl patch daemonsets -n projectcontour envoy -p '{"spec":{"template":{"spec":{"nodeSelector":{"ingress-ready":"true"},"tolerations":[{"key":"node-role.kubernetes.io/master","operator":"Equal","effect":"NoSchedule"}]}}}}'
```

Install the ingress (NGINX):
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/kind/deploy.yaml
```

Use `ingress/ingress.yaml` file with:
```
kubectl apply -f ingress/ingress.yaml
```

### Enable multinode

Create a Multi-node Kubernetes local cluster with:
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

**Note**: This is necessary only if you didn't install the ingress.

Then open the following URL [localhost:8080](http://localhost:8080) in a browser
to access to the Web application.

![](/img/front.png)

### Accessing to the dashboards

Open the following URLs in a browser:

* Access to [grafana's dashboard](http://localhost:8080/grafana) with: `http://localhost:8080/grafana`

* Access to [kibana's dashboard](http://localhost:8080/kibana) with: `http://localhost:8080/kibana`

* Access to [jaeger's dashboard](http://localhost:8080/jaeger) with: `http://localhost:8080/jaeger`

* Access to [prometheus' dashboard](http://localhost:8080/prometheus) with: `http://localhost:8080/prometheus`

* Access to swagger's dashboard:
  * Employee: [localhost:8080/employee/swagger-ui](http://localhost:8080/employee/swagger-ui)
  * Department: [localhost:8080/department/swagger-ui](http://localhost:8080/department/swagger-ui)
  * Human Resources: [localhost:8080/hr/swagger-ui](http://localhost:8080/hr/swagger-ui)

## Load images to avoid errors

The first time all the services are deployed in Kubernetes, the nodes has to
download all the images in YAML files to the internal Docker's Registry. This may
cause some error at the first time. Try to load all the images to the nodes from
your local registry with:
```
kind load docker-image docker.elastic.co/elasticsearch/elasticsearch-oss:6.8.2 && \
kind load docker-image docker.elastic.co/logstash/logstash-oss:6.8.2 && \
kind load docker-image docker.elastic.co/kibana/kibana-oss:6.8.2 && \
kind load docker-image serrodcal/employee-native:1.0.1 && \
kind load docker-image serrodcal/department-native:1.0.1 && \
kind load docker-image serrodcal/hr-native:1.0.1 && \
kind load docker-image postgres:10.5 && \
kind load docker-image prom/prometheus:v2.17.1 && \
kind load docker-image grafana/grafana:6.7.2 && \
kind load docker-image jaegertracing/all-in-one:1.17.1
```

## Author

* [Serrodcal](https://github.com/serrodcal)
* [Lechowsky](https://github.com/lechowsky)
* [Jualoppaz](https://github.com/jualoppaz)
