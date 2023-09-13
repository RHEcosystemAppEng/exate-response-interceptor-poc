```shell
mvn io.quarkus.platform:quarkus-maven-plugin:3.3.2:create\
  -DprojectGroupId=com.redhat.interceptor\
  -DprojectArtifactId=intercepting-proxy-service\
  -Dextensions="reactive-routes,hibernate-validator,container-image-docker"\
  -DnoCode
```

## Request Headers

| Header Key         | Description        | Required | Default Value |
|--------------------|--------------------|----------|---------------|
| Api-Gator-Bypass   | Bypass API Gator   | No       | false         | 

## Application Properties

> Visit [APIGator Docs][1] for possible values and types.

| Property Name                      | Environment Variable               | Required | Default Value                |
|------------------------------------|------------------------------------|:--------:|------------------------------|
| target.server.host                 | TARGET_SERVER_HOST                 |    No    | localhost                    |
| target.server.port                 | TARGET_SERVER_PORT                 |   Yes    |                              |
| target.server.secure               | TARGET_SERVER_SECURE               |    No    | false                        |
| api.gator.host                     | API_GATOR_HOST                     |    No    | api.exate.co                 |
| api.gator.port                     | API_GATOR_API                      |    No    | 443                          |
| api.gator.dataset-uri              | API_GATOR_DATASET_URI              |    No    | /apigator/protect/v1/dataset |
| api.gator.token-uri                | API_GATOR_TOKEN_URI                |    No    | /apigator/identity/v1/token  |
| api.gator.api-key                  | API_GATOR_API_KEY                  |   Yes    |                              |
| api.gator.client-id                | API_GATOR_CLIENT_ID                |   Yes    |                              |
| api.gator.client-secret            | API_GATOR_CLIENT_SECRET            |   Yes    |                              |
| api.gator.grant-type               | API_GATOR_GRANT_TYPE               |    No    | client_credentials           |
| api.gator.manifest-name            | API_GATOR_MANIFEST_NAME            |   Yes    |                              |
| api.gator.job-type                 | API_GATOR_JOB_TYPE                 |   Yes    |                              |
| api.gator.country-code             | API_GATOR_COUNTRY_CODE             |   Yes    |                              |
| api.gator.data-owning-country-code | API_GATOR_DATA_OWNING_COUNTRY_CODE |    No    |                              |
| api.gator.data-usage-id            | API_GATOR_DATA_USAGE_ID            |    No    |                              |
| api.gator.protect-null-values      | API_GATOR_PROTECT_NULL_VALUES      |   Yes    |                              |
| api.gator.restricted-text          | API_GATOR_RESTRICTED_TEXT          |    No    |                              |
| api.gator.preserve-string-length   | API_GATOR_PRESERVE_STRING_LENGTH   |   Yes    |                              |
| api.gator.sql-type                 | API_GATOR_SQL_TYPE                 |    No    |                              |
| api.gator.classification-model     | API_GATOR_CLASSIFICATION_MODEL     |    No    |                              |
| api.gator.third-party-name         | API_GATOR_THIRD_PARTY_NAME         |    No    |                              |
| api.gator.third-party-id           | API_GATOR_THIRD_PARTY_ID           |    No    |                              |


<!-- Links -->
[1]: https://developer.exate.co/catalog/api/b49306b2-4040-429e-9306-b24040129ea1/doc
