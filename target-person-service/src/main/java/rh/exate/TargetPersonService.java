package rh.exate;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.logging.Logger;

record Person (String id, String firstName, String lastName, int age, String accessToken, String secret){}

@Path("/v1/api/customers")
public class TargetPersonService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson(@NotNull @RestQuery String id) {
        return new Person(id,"John","doe",50,"someSecretAccessTokenValue","someConfidentialSecretValue");
    }

}
