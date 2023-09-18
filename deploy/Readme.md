# Exate POC Deployment Guide

This README provides instructions for deploying the mock service and the interceptor app on OpenShift using provided YAML configuration files.

## Prerequisites

- Access to an OpenShift cluster.
- `oc` command-line tool installed and authenticated.

## Deploying the Services

### 1. Mock Service

To deploy the `target-person-service`, follow the instructions below:

```bash
oc apply -f target-person-service-deployment.yaml
oc apply -f target-person-service-service.yaml
```

### 2. Interceptor App

```bash
oc apply -f interceptor-config.yaml
oc apply -f gator-api-interceptor-deployment.yaml
oc apply -f gator-api-interceptor-service.yaml
```

## Testing the Setup


1. **Verify the original dataset:**

```bash

curl -sw "\n" "https://target-person-service-exate-api-gator.apps.ocp-dev01.lab.eng.tlv2.redhat.com/v1/api/customers?id=56" -v -k

```

2. **Verify the masked dataset:**

```bash
curl -sw "\n" "https://gator-api-interceptor-exate-api-gator.apps.ocp-dev01.lab.eng.tlv2.redhat.com/v1/api/customers?id=56" -v -k
```

3. **Bypass the interceptor:**

```bash
curl -H "Api-Gator-Bypass: true" -sw "\n" "https://target-person-service-exate-api-gator.apps.ocp-dev01.lab.eng.tlv2.redhat.com/v1/api/customers?id=56" -v -k
```

---


# Exate POC Deployment Guide

This README provides instructions for deploying the mock service and the interceptor app using the `Makefile`.

## Prerequisites

- Access to an OpenShift cluster.
- `oc` command-line tool installed and authenticated.
- `make` tool installed.

## Makefile Usage

The provided `Makefile` simplifies the deployment and testing processes for the services. Below are the commands you can run:

### Deployment

1. **Deploy the Interceptor:**
   ```bash
   make deploy-interceptor
    ```

2. **Deploy the Target Service:**
   ```bash
   make deploy-target
   ```

3. **Deploy Both Services Together:**
   ```bash
   make deploy
   ```



### Testing

1. **Verify the Original Dataset:**
   ```bash
   make test-original
    ```

2. **Verify the Masked Dataset:**
   ```bash
   make test-masked
   ```

3. **Bypass the Interceptor and Verify Dataset:**
   ```bash
   make test-bypass
   ```

4. **Run All Tests Together:**
   ```bash
   make test
   ```


