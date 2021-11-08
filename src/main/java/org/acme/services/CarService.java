package org.acme.services;

import org.acme.model.Car;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@ApplicationScoped
public class CarService {
    private static final Logger log = Logger.getLogger(CarService.class.getName());

    private AtomicInteger sequence = new AtomicInteger();
    private List<Car> cars = Collections.synchronizedList(new ArrayList<>());

    public Integer create(Car car) {
        car.setId(sequence.incrementAndGet());
        cars.add(car);
        log.info("Entity saved: id=" + sequence.get());

        return car.getId();
    }

    public List<Car> list() {
        log.info("List requested");

        return cars;
    }

    public Car find(Integer id) {
        log.info("Entity requested: id=" + id);

        return cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst().get();
    }

    public void remove(Integer id) {
        log.info("Entity removed: id=" + id);

        cars.remove(cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().get());
    }
}
