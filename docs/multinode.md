# Enable multinode

Create a Multi-node Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-config.yaml
```

In this approach, there is no Ingress Controler, due to this you have
to make port forwaring:

```
kubectl port-forward <gateway_pod_name> 8080
```

Add 8080 port to any URL to be able to access to the dashboard. 
