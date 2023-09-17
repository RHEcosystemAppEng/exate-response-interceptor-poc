# Exate API-Gator Interceptor POC

[Target Person Service](target-person-service/README.md) is a mock service returning a dummy JSON that will be used as the target application to
intercept using the Interceptor App.

<details>
<summary>Run POC Locally</summary>

### Run the mock service

```shell
podman run --rm -p 8080:8080 quay.io/ecosystem-appeng/exate-poc-target-person-service:1.0.0-SNAPSHOT
```

#### Verify original dataset

```shell
curl -sw "\n" http://localhost:8080/v1/api/customers?id=56
```

```json
{"employees":{"employee":[{"id":"56","firstName":"John","lastName":"doe","age":51,"accessToken":"someSecretAccessTokenValue","secret":"someConfidentialSecretValue"}]}}
```

### Run the interceptor app

> Note **network is set to host** in order to make the mock service accessible to the interceptor app.<br/>
> Also note the usage of *TARGET_SERVER_PORT* introducing the mock service app. The *TARGET_SERVER_HOST* defaults to
> *localhost*.<br/>
> Also note and replace the values of *API_GATOR_API_KEY*, *API_GATOR_CLIENT_ID*, *API_GATOR_CLIENT_SECRET*.

```shell
podman run --rm -p 8082:8082 --network=host \
-e TARGET_SERVER_PORT='8080' \
-e API_GATOR_API_KEY='api-key-goes-here' \
-e API_GATOR_CLIENT_ID='client-id-goes-here' \
-e API_GATOR_CLIENT_SECRET='client-secret-goes-here' \
-e API_GATOR_MANIFEST_NAME='Employee' \
-e API_GATOR_DATASET_TYPE='JSON' \
-e API_GATOR_JOB_TYPE='DataMasking' \
-e API_GATOR_COUNTRY_CODE='GB' \
-e API_GATOR_PROTECT_NULL_VALUES='false' \
-e API_GATOR_PRESERVE_STRING_LENGTH='true' \
quay.io/ecosystem-appeng/gator-api-interceptor:1.0.0-SNAPSHOT
```

#### Verify the masked dataset

> Note the usage of port 8082, the interceptor port, instead of port 8080, the target mock service port. 

```shell
curl -sw "\n" http://localhost:8082/v1/api/customers?id=56
```

```json
{
    "employees": {
        "employee": [
            {
                "id": "56",
                "firstName": "3968",
                "lastName": "1b6",
                "age": 51,
                "accessToken": "someSecretAccessTokenValue",
                "secret": "someConfidentialSecretValue"
            }
        ]
    }
}
```

#### Verify bypassing API-Gator

```shell
curl -H "Api-Gator-Bypass: true" -sw "\n" http://localhost:8082/v1/api/customers?id=56
```

```json
{"employees":{"employee":[{"id":"56","firstName":"John","lastName":"doe","age":51,"accessToken":"someSecretAccessTokenValue","secret":"someConfidentialSecretValue"}]}}
```

</details>
