package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.model.Car;
import org.acme.resources.WireMockWordsResource;
import org.acme.services.CarRepository;
import org.acme.services.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@QuarkusTest
@QuarkusTestResource(WireMockWordsResource.class)
public class CarServiceTest {

    @Inject
    CarService carService;

    private Car car;

    @Test
    public void testCreateCar() {
        car = new Car(null, "Ford", "Focus", "Yellow", 2003);

        Assertions.assertNotNull(carService.create(car));
    }

    @Test
    public void testRetrieveCar() {
        car = new Car(null, "Renault", "Clio", "Blue", 1998);
        car.setId(carService.create(car));

        Assertions.assertEquals(car, carService.find(car.getId()));
    }

    @Test
    public void testListCars() {
        List<Car> cars = Arrays.asList(
                new Car(null, "Mercedes", "A3", "White", 2010),
                new Car(null, "BMW", "300", "Black", 2011)
        );

        cars.forEach(carService::create);

        List<Car> carResponse = carService.list();

        Assertions.assertTrue(cars.size() >= 2);
        Assertions.assertTrue(carResponse.containsAll(cars));
    }

    @Test
    public void testDeleteCar() {
        car = new Car(null, "Opel", "Vectra", "Orange", 2005);
        car.setId(carService.create(car));
        carService.remove(car.getId());

        Assertions.assertNull(carService.find(car.getId()));
    }

    @Test
    public void testAdditionalTextGenerator() {
        String response = carService.getAdditionalText("test");

        Assertions.assertEquals("word1 word2 word3 word4 word5", response);
    }
}
