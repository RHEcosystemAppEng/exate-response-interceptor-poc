IMAGE_GROUP ?= ecosystem-appeng
IMAGE_NAME ?= exate-poc-target-person-service
IMAGE_REGISTRY ?= quay.io

.PHONY: push/target-person-service
push/target-person-service:
	./target-person-service/mvnw install \
	-Dquarkus.container-image.build=true \
	-Dquarkus.container-image.push=true \
	-Dquarkus.container-image.group=$(IMAGE_GROUP) \
	-Dquarkus.container-image.name=$(IMAGE_NAME) \
	-Dquarkus.container-image.registry=$(IMAGE_REGISTRY) \
	-f target-person-service/pom.xml
