# Makefile for deploying and testing services

# Variables
NAMESPACE := $(or $(shell echo $$NAMESPACE),exate-api-gator)
ROUTER_DOMAIN := $(or $(shell echo $$ROUTER_DOMAIN),apps.ocp-dev01.lab.eng.tlv2.redhat.com)


INTERCEPTOR_DEPLOYMENT = gator-api-interceptor-deployment.yaml
INTERCEPTOR_SERVICE = gator-api-interceptor-service.yaml
INTERCEPTOR_ROUTE = gator-api-interceptor-route.yaml
INTERCEPTOR_CONFIG = interceptor-config.yaml
TARGET_SERVICE_DEPLOYMENT = target-person-service-deployment.yaml
TARGET_SERVICE_SERVICE = target-person-service-service.yaml
TARGET_SERVICE_ROUTE = target-person-service-route.yaml


# Deployment rules
deploy-interceptor:
	@oc apply -f $(INTERCEPTOR_CONFIG) -n $(NAMESPACE)
	@oc apply -f $(INTERCEPTOR_DEPLOYMENT) -n $(NAMESPACE)
	@oc apply -f $(INTERCEPTOR_SERVICE) -n $(NAMESPACE)
	@oc create -f $(INTERCEPTOR_ROUTE) -n $(NAMESPACE)

deploy-target:
	@oc apply -f $(TARGET_SERVICE_DEPLOYMENT) -n $(NAMESPACE)
	@oc apply -f $(TARGET_SERVICE_SERVICE) -n $(NAMESPACE)
	@oc create -f $(TARGET_SERVICE_ROUTE) -n $(NAMESPACE)

# Testing rules
test-original:
	@echo "\n========================================"
	@echo "\nTesting Original Dataset...\n"
	@curl -sw "\n" "https://target-person-service-$(NAMESPACE).$(ROUTER_DOMAIN)/v1/api/customers?id=56" -k
	@echo "========================================\n"

test-masked:
	@echo "\n========================================"
	@echo "\n\nTesting Masked Dataset...\n"
	@curl -sw "\n" "https://gator-api-interceptor-$(NAMESPACE).$(ROUTER_DOMAIN)/v1/api/customers?id=56" -k
	@echo "========================================\n"

test-bypass:
	@echo "\n========================================"
	@echo "\n\nTesting Bypass of the Interceptor...\n"
	@curl -H "Api-Gator-Bypass: true" -sw "\n" "https://target-person-service-$(NAMESPACE).$(ROUTER_DOMAIN)/v1/api/customers?id=56" -k
	@echo "========================================\n"

# Combined testing rule
test: test-original test-masked test-bypass

# Combined deploy rule
deploy: deploy-interceptor deploy-target

# Clean rule to undeploy services
clean:
	@oc delete -f $(INTERCEPTOR_CONFIG)
	@oc delete -f $(INTERCEPTOR_DEPLOYMENT)
	@oc delete -f $(INTERCEPTOR_SERVICE)
	@oc delete -f $(INTERCEPTOR_ROUTE)
	@oc delete -f $(TARGET_SERVICE_DEPLOYMENT)
	@oc delete -f $(TARGET_SERVICE_SERVICE)
	@oc delete -f $(TARGET_SERVICE_ROUTE)

.PHONY: deploy-interceptor deploy-target test-original test-masked test-bypass test deploy clean
