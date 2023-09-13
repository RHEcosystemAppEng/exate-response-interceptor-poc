package rh.exate;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

record Person (String id, String firstName, String lastName, int age, String accessToken, String secret){}

record Employee(List<Person> employee){}

record Employees(Employee employees){}

@Path("/v1/api/customers")
public class TargetPersonService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Employees getPerson(@NotNull @RestQuery String id) {
        // the response structure matches the Employee manifest pre-configured in gator and used in the poc
        var person = new Person(id,"John","doe",51,"someSecretAccessTokenValue","someConfidentialSecretValue");
        var employee = new Employee(List.of(person));
        return new Employees(employee);
    }

}
