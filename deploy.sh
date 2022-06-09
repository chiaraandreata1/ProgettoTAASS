#./mvnw -DskipTests=true clean package

# Build docker containers
for d in auth-service facility-service gateway lesson-service reservation-service tournament-service sessiondb; do
  cd $d
  echo $d
  docker build -t $d .
  cd -
done

echo frontend
cd frontend
docker build -t frontned dist
cd -

# Run databases
#kubect apply -f rabbit.yaml 

