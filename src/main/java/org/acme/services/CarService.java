package org.acme.services;

import org.acme.model.Car;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarService {
    private static final Logger log = Logger.getLogger(CarService.class.getName());

    private AtomicInteger sequence = new AtomicInteger();
    private List<Car> cars = Collections.synchronizedList(new ArrayList<>());

    @Inject
    @RestClient
    WordsService wordsService;

    public Integer create(Car car) {
        car.setId(sequence.incrementAndGet());
        car.setAdditionalText(getAdditionalText(car.getModel()));
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

    public String getAdditionalText(String keyWord) {
        try {
            return wordsService.getWords(keyWord, 10)
                    .stream().map(c -> c.word)
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            return "WordsService unavailable";
        }
    }

    public void update(Integer id, Car car) {
        log.info("Entity updated" + id);
        remove(id);
        car.setId(id);
        car.setAdditionalText(getAdditionalText(car.getModel()));
        cars.add(car);
    }
}
