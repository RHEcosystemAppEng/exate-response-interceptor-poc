.PHONY: push/target-person-service
push/target-person-service: ## Push quay.io/ecosystem-appeng/exate-poc-target-person-service
	./target-person-service/mvnw install -Dquarkus.container-image.push=true -f target-person-service/pom.xml
