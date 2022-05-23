#!/bin/bash

minikube start
eval $(minikube docker-env)


(cd database/; ./mvnw package -Dmaven.test.skip=true)
docker build -t h2-service database/
minikube kubectl -- apply -f database/H2Deployment.yaml
minikube kubectl -- apply -f database/H2-svc.yaml	

(cd Wallet/; ./mvnw package -Dmaven.test.skip=true)
docker build -t wallet-service Wallet/
minikube kubectl -- apply -f Wallet/WalletDeployment.yaml
minikube kubectl -- apply -f Wallet/Wallet-svc.yaml

(cd Restaurant/; ./mvnw package -Dmaven.test.skip=true)
docker build -t restaurant-service Restaurant/
minikube kubectl -- apply -f Restaurant/RestaurantDeployment.yaml
minikube kubectl -- apply -f Restaurant/Restaurant-svc.yaml

while [ "$(minikube kubectl -- get pods -l=app='akash-restaurant' -o jsonpath='{.items[*].status.containerStatuses[0].ready}')" != "true" ]; do
   sleep 3
   echo "Restaurant container still not ready, so waiting 3 sec."
done
sleep 3
minikube kubectl -- port-forward svc/akash-restaurant 8080:8080 >/dev/null &


while [ "$(minikube kubectl -- get pods -l=app='akash-wallet' -o jsonpath='{.items[*].status.containerStatuses[0].ready}')" != "true" ]; do
   sleep 3
   echo "Wallet container still not ready, so waiting 3 sec."
done
sleep 3
minikube kubectl -- port-forward svc/akash-wallet 8082:8082 >/dev/null &


(cd Delivery/; ./mvnw package -Dmaven.test.skip=true)
docker build -t delivery-service Delivery/
minikube kubectl -- apply -f Delivery/DeliveryDeployment.yaml
minikube kubectl -- apply -f Delivery/Delivery-svc.yaml


while [ "$(minikube kubectl -- get pods -l=app='akash-h2' -o jsonpath='{.items[*].status.containerStatuses[0].ready}')" != "true" ]; do
   sleep 3
   echo "H2 container still not ready, so waiting 3 sec."
done
sleep 3
minikube kubectl -- port-forward svc/akash-h2 8083:8083 >/dev/null &


sleep 3

while [ "$(minikube kubectl -- get pods -l=app='akash-delivery' -o jsonpath='{.items[*].status.containerStatuses[0].ready}')" != "true" ]; do
   sleep 3
   echo "Delivery container still not ready, so waiting 3 sec."
done
sleep 3
minikube kubectl -- port-forward svc/akash-delivery 8081:8081 > /dev/null &

minikube kubectl -- apply -f Delivery/autoscale.yaml

exit



