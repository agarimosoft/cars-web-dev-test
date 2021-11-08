package org.acme.rest;

import org.acme.model.Car;
import org.acme.services.CarService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarsResource {

    @Inject
    CarService carService;

    @POST
    public Response create(Car car) {
        if (car.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Response.ok(carService.create(car)).status(Response.Status.CREATED).build();
    }

    @GET
    public Response list() {
        return Response.ok(carService.list()).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Integer id) {
        return Response.ok(carService.find(id)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Integer id) {
        carService.remove(id);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Integer id, Car car) {
        if (car.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        carService.update(id, car);
        return Response.ok().build();
    }
}
