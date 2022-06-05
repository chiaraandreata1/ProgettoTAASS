kubectl delete deployment/dbsession
kubectl delete configmap/postgres-session-config
kubectl delete pvc/postgres-session-pv-claim
kubectl delete pv/postgres-session-pv-volume
kubectl delete service/dbsession
