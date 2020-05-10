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
kind create cluster --config kind/kind-ingress-config.yaml
```

**Note**: Use `--image kindest/node:<target_version>` if you want to choose different version of k8s.

### Enable Ingress

![](/img/ingress.png)

Use `ingress/ingress.yaml` file with:
```
kubectl apply -f k8s/ingress.yaml
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

## Authors

* [Serrodcal](https://github.com/serrodcal)

## Colaborators

* [Lechowsky](https://github.com/lechowsky)
* [Jualoppaz](https://github.com/jualoppaz)
