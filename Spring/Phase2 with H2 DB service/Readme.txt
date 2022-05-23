



////////////////////////////////////// Phase2 readme /////////////////////////////////////////////////////////////////////
Extract the zip
enter the extracted zip folder.
There must Folders (Wallet, Restaurant, Delivery, Scripts & database), and Scripts(launch.sh, teardown.sh & autoscale.yaml)
Instructions to run:
1. open terminal and run ./launch.sh
2. open another terminal and run minikube tunnel
3. Run all the testcases
4. run ./teardown.sh
















////////////////////////////////////// Phase1 readme /////////////////////////////////////////////////////////////////////

Dependencies required:

Below are the instructions
1. Paste a copy of initialData.txt in Downloads folders
2. Extract the zip file
3. Open terminal and perform the below commands inside Restaurant directory 
	a) ./mvnw package
	b) docker build -t restaurant-service .
	c) docker run -p 8080:8080 --rm --name restaurant --add-host=host.docker.internal:host-gateway -v ~/Downloads/initialData.txt:/initialData.txt restaurant-service
	
4. Open terminal and perform the below commands inside Delivery directory 
	a) ./mvnw package
	b) docker build -t delivery-service .
	c) docker run -p 8081:8080 --rm --name delivery --add-host=host.docker.internal:host-gateway -v ~/Downloads/initialData.txt:/initialData.txt delivery-service
	
5. Open terminal and perform the below commands inside Wallet directory 
	a) ./mvnw package
	b) docker build -t wallet-service .
	c) docker run -p 8082:8080 --rm --name wallet --add-host=host.docker.internal:host-gateway -v ~/Downloads/initialData.txt:/initialData.txt wallet-service
	
6. Now once all the services start running, we can send various different kinds of requests which we want to test.
7. use "docker stop wallet" to stop wallet service
8. use "docker stop delivery" to stop delivery service
9. use "docker stop restaurant" to stop restaurant service

***** Use "./mvnw package -Dmaven.test.skip=true" if maven build fails. Spring initializer was used which added extra testcases.******


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

1) spring-boot-starter-web
2) jackson-core
	version: 2.13.1

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-core</artifactId>
			<version>2.13.1</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
