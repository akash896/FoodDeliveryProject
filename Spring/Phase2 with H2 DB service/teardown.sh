#!/bin/bash

fuser -k 8080/tcp
fuser -k 8081/tcp
fuser -k 8082/tcp
fuser -k 8083/tcp

minikube kubectl -- delete -f database/H2Deployment.yaml
minikube kubectl -- delete -f database/H2-svc.yaml

minikube kubectl -- delete -f Wallet/WalletDeployment.yaml
minikube kubectl -- delete -f Wallet/Wallet-svc.yaml

minikube kubectl -- delete -f Restaurant/RestaurantDeployment.yaml
minikube kubectl -- delete -f Restaurant/Restaurant-svc.yaml

minikube kubectl -- delete -f Delivery/DeliveryDeployment.yaml
minikube kubectl -- delete -f Delivery/Delivery-svc.yaml

minikube kubectl -- delete -f Delivery/autoscale.yaml

minikube stop
