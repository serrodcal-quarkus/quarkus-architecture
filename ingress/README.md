# Enable Ingress Controller

According to [this page](https://kind.sigs.k8s.io/docs/user/ingress/), it is possible
to create an ingress controller to assign an static IP to your cluster and also
to be able to call the components using an ingress and avoiding port forwarding.

Just create the cluster with:
```
kind create cluster --config kind/kind-ingress-config.yaml
```

Or, create a Multi-node Kubernetes local cluster with:
```
kind create cluster --config kind/kind-ha-ingress-config.yaml
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
