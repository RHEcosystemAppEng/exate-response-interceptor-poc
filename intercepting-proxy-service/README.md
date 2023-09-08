```shell
mvn io.quarkus.platform:quarkus-maven-plugin:3.3.2:create\
  -DprojectGroupId=com.redhat.interceptor\
  -DprojectArtifactId=intercepting-proxy-service\
  -Dextensions="reactive-routes,container-image-docker"\
  -DnoCode
```
