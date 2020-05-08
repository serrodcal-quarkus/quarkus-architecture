# Load images to avoid errors

The first time all the services are deployed in Kubernetes, the nodes has to
download all the images in YAML files to the internal Docker's Registry. This may
cause some error at the first time. Try to load all the images to the nodes from
your local registry with:
```
kind load docker-image docker.elastic.co/elasticsearch/elasticsearch-oss:6.8.2 && \
kind load docker-image docker.elastic.co/logstash/logstash-oss:6.8.2 && \
kind load docker-image docker.elastic.co/kibana/kibana-oss:6.8.2 && \
kind load docker-image serrodcal/employee-native:1.1.0 && \
kind load docker-image serrodcal/department-native:1.1.0 && \
kind load docker-image serrodcal/hr-native:1.1.0 && \
kind load docker-image serrodcal/hr-frontend-server:1.0.0-alpine
kind load docker-image postgres:10.5 && \
kind load docker-image prom/prometheus:v2.17.1 && \
kind load docker-image grafana/grafana:6.7.2 && \
kind load docker-image jaegertracing/all-in-one:1.17.1 && \
kind load docker-image nginx:1.17.10-alpine
```

**Note**: before, those images has to be in your local registry.
