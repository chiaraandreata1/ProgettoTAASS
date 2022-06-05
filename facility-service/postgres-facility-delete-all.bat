kubectl delete deployment/dbfacility
kubectl delete configmap/postgres-facility-config
kubectl delete pvc/postgres-facility-pv-claim
kubectl delete pv/postgres-facility-pv-volume
kubectl delete service/dbfacility
