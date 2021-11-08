package org.acme.services;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.model.Car;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public Car findbyMakeAndModel(String make, String model) {
        Map<String, Object> params = new HashMap<>();
        params.put("make", make);
        params.put("model", model);

        return find("FROM Car WHERE make= :make AND model= :model ", params).firstResult();
    }
}
