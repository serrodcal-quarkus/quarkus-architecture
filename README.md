# quarkus-prometheus

![](/img/quarkus.png)

Sample Quarkus application with the following dependencies:

* [Microprofile metrics](https://quarkus.io/guides/microprofile-metrics)
* [Microprofile health](https://quarkus.io/guides/microprofile-health)
* [Opentracing](https://quarkus.io/guides/opentracing)
* [Open API and Swagger](https://quarkus.io/guides/openapi-swaggerui)

## Endpoints


## Creating Kubernetes local cluster

Create a Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-config.yaml
```

## Deploying application

Deploy the application with:
```
kubectl apply -f k8s/
```

## Exposing ports

Expose the application with:
```
kubectl port-forward <employee_pod_name> 8080:8080
```

Expose the prometheus' dashboard with:
```
kubectl port-forward <prometheus_pod_name> 9090:9090
```

## Endpoints

Swagger file is given below:
```yaml
---
openapi: 3.0.1
info:
  title: Employee API
  version: "1.0.0"
paths:
  /quarkus/employee:
    get:
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/MultiEmployee'
    put:
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Employee'
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UniResponse'
    post:
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Employee'
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UniResponse'
  /quarkus/employee/{id}:
    get:
      parameters:
      - name: id
        in: path
        required: true
        schema:
          format: int64
          type: integer
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UniEmployee'
    delete:
      parameters:
      - name: id
        in: path
        required: true
        schema:
          format: int64
          type: integer
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UniResponse'
components:
  schemas:
    Employee:
      type: object
      properties:
        id:
          format: int64
          type: integer
        name:
          type: string
    UniResponse:
      type: object
    UniEmployee:
      type: object
    MultiEmployee:
      type: object
```

Access to prometheus dashboard with: [localhost:9090](http:/localhost:9090)

## Author

* [Serrodcal](https://github.com/serrodcal)
