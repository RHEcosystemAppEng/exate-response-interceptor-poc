# Target Person Service

## Creation

```shell
# from the project's root
mvn io.quarkus.platform:quarkus-maven-plugin:3.3.2:create\
  -DprojectGroupId=rh.exate\
  -DprojectArtifactId=target-person-service\
  -Dextensions="resteasy-reactive,resteasy-reactive-jackson,hibernate-validator,container-image-docker"\
  -DnoCode
```

```shell
# from the project's root
mkdir -p target-person-service/src/main/java/rh/exate
```

```shell
# from the project's root
cat << EOF >> target-person-service/src/main/java/rh/exate/TargetPersonService.java
package rh.exate;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

record Person (String id, String firstName, String lastName, int age, String accessToken, String secret){}

@Path("/v1/api/customers")
public class TargetPersonService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(@NotNull @RestQuery String id) {
        return new Person(id,"John","doe",50,"someSecretAccessTokenValue","someConfidentialSecretValue");
    }

}

EOF
```


## Usage

```shell
curl -s http://localhost:8080/v1/api/customers?id=17 | jq
```

```json
{
  "id": "17",
  "firstName": "John",
  "lastName": "doe",
  "age": 50,
  "accessToken": "someSecretAccessTokenValue",
  "secret": "someConfidentialSecretValue"
}
```

## Deploy

```shell
./mvnw install -Dquarkus.container-image.push=true
```

> Image spec is set in [src/main/resources/application.properties](src/main/resources/application.properties)

The image will be available at `quay.io/ecosystem-appeng/exate-poc-target-person-service`.