package org.acme.services;

import org.acme.model.Car;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarService {
    private static final Logger log = Logger.getLogger(CarService.class.getName());

    @Inject
    @RestClient
    WordsService wordsService;

    @Inject
    CarRepository carRepository;

    @Transactional
    public Long create(Car car) {
        car.setAdditionalText(getAdditionalText(car.getModel()));
        carRepository.persist(car);

        log.info("Entity saved: id=" + car.getId());

        return car.getId();
    }

    public List<Car> list() {
        log.info("List requested");

        return carRepository.listAll();
    }

    public Car find(Long id) {
        log.info("Entity requested: id=" + id);

        return carRepository.findById(id);
    }

    public Car findByMakeAndModel(String make, String model) {
        log.info("Entity requested: make=" + make + " model="+ model);

        return carRepository.findbyMakeAndModel(make, model);
    }

    @Transactional
    public void remove(Long id) {
        log.info("Entity removed: id=" + id);

        carRepository.deleteById(id);
    }

    public String getAdditionalText(String keyWord) {
        try {
            return wordsService.getWords(keyWord, 5)
                    .stream().map(c -> c.word)
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            return "WordsService unavailable";
        }
    }

    @Transactional
    public void update(Long id, Car car) {
        log.info("Entity updated: id=" + id);

        Car entity = find(id);
        entity.setMake(car.getMake());
        entity.setModel(car.getModel());
        entity.setAdditionalText(getAdditionalText(car.getModel()));
        entity.setColour(car.getColour());
        entity.setYear(car.getYear());
    }
}
