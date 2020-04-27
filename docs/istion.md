# Istio

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

## Clean up Istio

Remove all Istio components with:
```
istioctl manifest generate --set profile=demo | kubectl delete -f -
```

## Kiali's dashboard

Access to kiali's Dashboard (`admin;admin`) provided by Istio (only if you enable Istio):
```
istioctl dashboard kiali
```
