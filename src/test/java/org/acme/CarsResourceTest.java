package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.model.Car;
import org.acme.services.CarService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CarsResourceTest {

    @Inject
    CarService carService;

    @Test
    public void testCreateCar() {
        given()
                .body("{\"make\": \"Ford\", \"model\": \"Focus\", \"colour\": \"White\", \"year\": 1994}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/cars")
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateInvalidCar() {
        given()
                .body("{\"id\": 1,\"make\": \"Ford\", \"model\": \"Focus\", \"colour\": \"White\", \"year\": 1994}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/cars")
                .then()
                .statusCode(422);
    }

    @Test
    public void testRetrieveCar() {
        Long id = carService.create(new Car(null, "Honda", "Civic", "Blue", 2005));

        given()
                .when().get("/cars/" + id)
                .then()
                .statusCode(200)
                .body(
                        "make", is("Honda"),
                        "model", is("Civic"),
                        "colour", is("Blue"),
                        "year", is(2005)
                );
    }

    @Test
    public void testList() {
        List<Car> cars = Arrays.asList(
                new Car(null, "Mercedes", "A3", "White", 2010),
                new Car(null, "BMW", "300", "Black", 2011)
        );

        cars.forEach(carService::create);

        given()
                .when().get("/cars")
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.greaterThanOrEqualTo(2));
    }

    @Test
    public void testDeleteCar() {
        Long id = carService.create(new Car(null, "Opel", "Vectra", "Green", 2007));

        given()
                .when().delete("/cars/" + id)
                .then()
                .statusCode(200);

        Assertions.assertNull(carService.find(id));
    }

    @Test
    public void testUpdateCar() {
        Long id = carService.create(new Car(null, "Renault", "Clio", "Blue", 1998));

        given()
                .body("{\"make\": \"Renault\", \"model\": \"Polo\", \"colour\": \"Blue\", \"year\": 1994}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .put("/cars/" + id)
                .then()
                .statusCode(200);

        given()
                .when().get("/cars/" + id)
                .then()
                .statusCode(200)
                .body(
                        "make", is("Renault"),
                        "model", is("Polo"),
                        "colour", is("Blue"),
                        "year", is(1994)
                );
    }

    @Test
    public void testUpdateInvalidCar() {
        Long id = carService.create(new Car(null, "Renault", "Clio", "Blue", 1998));

        given()
                .body("{\"id\": 1,\"make\": \"Renault\", \"model\": \"Clio\", \"colour\": \"Blue\", \"year\": 1998}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .put("/cars/" + id)
                .then()
                .statusCode(422);
    }
}
