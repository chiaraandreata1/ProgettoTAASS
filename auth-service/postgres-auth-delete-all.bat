kubectl delete deployment/dbusers
kubectl delete configmap/postgres-auth-config
kubectl delete pvc/postgres-auth-pv-claim
kubectl delete pv/postgres-auth-pv-volume
kubectl delete service/dbusers
