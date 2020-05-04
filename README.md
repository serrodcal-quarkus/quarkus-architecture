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
to be able to call the components using an ingress and avoiding port forwarding.

![](/img/ingress.png)

In this case, the port forward command below it isn't necessary at all.

Just create the cluster with:
```
kind create cluster --config kind/kind-ingress-config.yaml
```

Then, Install the ingress (NGINX):
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/kind/deploy.yaml
```

**Note**: Or if you prefer, use [Contour](https://kind.sigs.k8s.io/docs/user/ingress/#contour) instead.

Use `ingress/ingress.yaml` file with:
```
kubectl apply -f ingress/ingress.yaml
```

### Enable multinode

Create a Multi-node Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-config.yaml
```

### Enable multinode with ingress

Create a Multi-node Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-ingress-config.yaml
```

Finally, just follow the instruction above to set up Project Contour and NGINX Ingress Controller.

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

**Note**: This is necessary only if you didn't install the ingress. But, if you
prefer port forwarding way, please add `:8080` to all URL's below.

Then open the following URL [localhost/](http://localhost/) in a browser
to access to the Web application.

![](/img/front.png)

### Accessing to the dashboards

Open the following URLs in a browser:

* Access to [grafana's dashboard](http://localhost/grafana) with: `http://localhost/grafana`

* Access to [kibana's dashboard](http://localhost/kibana) with: `http://localhost/kibana`

* Access to [jaeger's dashboard](http://localhost/jaeger) with: `http://localhost/jaeger`

* Access to [prometheus' dashboard](http://localhost/prometheus) with: `http://localhost/prometheus`

* Access to swagger's dashboard:
  * Employee: [localhost/employee/swagger-ui](http://localhost/employee/swagger-ui)
  * Department: [localhost/department/swagger-ui](http://localhost/department/swagger-ui)
  * Human Resources: [localhost/hr/swagger-ui](http://localhost/hr/swagger-ui)

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
