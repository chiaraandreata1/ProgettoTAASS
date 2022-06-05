kubectl delete deployment/dbtournaments
kubectl delete configmap/postgres-tournament-config
kubectl delete pvc/postgres-tournament-pv-claim
kubectl delete pv/postgres-tournament-pv-volume
kubectl delete service/dbtournaments
