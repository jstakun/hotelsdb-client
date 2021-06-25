package net.gmsworld;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MongoClientApiTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/camel/v1/cache/test")
          .then()
             .statusCode(200)
             .contentType("application/json");
             //.body(is("Hello RESTEasy"));
        
        given()
        .when().get("/camel/v1/count/test")
        .then()
           .statusCode(200)
           //.contentType("application/json")
           .body(is("{\"count\": 11}"));
    }

}