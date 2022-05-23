echo Packaging for Delivery Service
cd Delivery
echo
mvn compile
# ./mvnw package

echo Packaging for Restaurant Service
cd ../Restaurant


echo Packaging for Wallet Service
cd ../Wallet
./mvnw package

# echo creating Docker Image: deli for Delivery Service
# cd ../Delivery
# docker build -t deli .

echo creating Docker Image: rest for Restaurant Service
cd ../Restaurant
docker build -t rest .

echo creating Docker Image: wall for Wallet Service
cd ../Wallet
docker build -t wall .

echo starting containers
cd ../Delivery

# mvn exec:java &
nohup mvn exec:java > deli.out &
# nohup docker run -p 8081:8080 --name deli deli > deli.out &
# cd ../Restaurant
# docker run -p 8080:8080 --name rest rest &
nohup docker run -p 8080:8080 --name rest rest > rest.out &
# cd ../Wallet
# docker run -p 8082:8080 --name wall wall &
nohup docker run -p 8082:8080 --name wall wall > wall.out &

echo waiting for the containers to boot for 40 sec.

# exit