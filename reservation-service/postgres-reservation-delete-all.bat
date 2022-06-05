kubectl delete deployment/dbreservations
kubectl delete configmap/postgres-reservation-config
kubectl delete pvc/postgres-reservation-pv-claim
kubectl delete pv/postgres-reservation-pv-volume
kubectl delete service/dbreservations
