package org.acme.services;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.model.Car;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {
}
