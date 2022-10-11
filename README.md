# smart-iot Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn compile quarkus:dev
```

## The main Tech Stack
We basically use Quarkus framework with it's extensions in order to ease the development and make it faster, also this project focused on to use GraalVM avoiding costly object allocations. Also it provides ability to build their own native images for developers.
1. GRPC extension with contract first development using protobuf
2. Hibernate reactive panache with mysql reactive client
3. Lombok to avoid boilerplate code and make it developer friendly
4. Quarkus Kubernetes development extensions to ease the configurations of k8s deployment scripts


## GRPC endpoints
1. saveSmartModel -- returns model id created
2. retrieveByModelId -- returns the exact model information with its' features
3. retrieveByModelName -- returns the every model information along with their features as a list.
4. retrieveByModelType -- returns the every model information along with their features as a list.
5. retrieveByModelCategory -- returns the every model information along with their features as a list.

## Current limitations and improvements needed
1. We assumed the data amount retrieving is significantly not huge, hence theirs no performance impact.
2. However, eventually the system can be improved more with streaming and pagination support in case handling multiple model information within seconds.
3. all the configurations should be externalized to in k8s yml deployment scripts based on the environment.
4. DB credentials should be maintained as secrets in k8s.
5. Apart from above, Security when required and DevSecOps pipeline setups, more junits should be accommodated, with several other factors. 

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application on local minikube cluster
> **_NOTE:_**  This guide is valid for Windows users, this can be slighlty adjusted according to your OS. By Installing Docker Desktop with simply activating minikube cluster would be helpful for your quick deployment similation and testing.

1. The application can be packaged and build the image:
```shell script
mvnw.cmd package -Pnative -Dquarkus.native.container-build=true
```
2. Dockerizing the image:
```shell script
docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/smart-iot:1.0 .
```
3. Creating the namespace in minikube:
```shell script
kubectl create namespace iot-dev
```
4. Creating a mysql instance within minikube(The following scripts can be found in k8s directory here.):
```shell script
kubectl apply -f pv.yml
kubectl apply -f mysql-deployment.yml
```
5. Deploy the pods and expose the service(minikube.yml script can be found in /target/kubernetes directory):
> **_NOTE:_**  kubernetes.yml file can be used within your own k8s cluster excepting minikube as minikube is not much stable we recommend to use minikube.yml which settings are with minimal bearable configurations.
```shell script
kubectl apply -f target/kubernetes/minikube.yml
expose the service by nodeport
```
## How to test the application
1. Download Postman, and create new GRPC request, switch to the url address bar, select to use server reflection, Postman will load all the endpoints and generate sample request formats to test the endpoints.
2. Also there is a test case which has been setup to test the grpc endpoints with grpc client and it has not been mocked, so it would provide some support you to test some of the endpoints.

## Troubleshoot the issues within your local PC
1. This application can be run on your local with minimal configuration changes, please take a note on the application.properties file to adjust the configurations as per your own settings. And simply start the application once configurations are changed by running SmartIxApp class.
2. To troubleshoot application within minikube, you can kubectl command as well as Docker Desktop to observe the logs and etc.
3. In addition to this, you may access k8s dashboard by generating a token by configuring an admin user and role. Once you issued the following commands,
4. You can browse to http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

   *The scripts can be found in k8s directory.
```shell script
kubectl create -f admin.yml
kubectl create -f roles.yml
kubectl -n kubernetes-dashboard create token admin-user
kubectl proxy
```

## Related Guides


## Provided Code

### gRPC

Create your first gRPC service

[Related guide section...](https://quarkus.io/guides/grpc-getting-started)
