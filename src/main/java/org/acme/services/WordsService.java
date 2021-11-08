package org.acme.services;

import org.acme.model.Word;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient
public interface WordsService {

    @GET
    @Path("/words")
    @Produces("application/json")
    List<Word> getWords(@QueryParam("sl") String word, @QueryParam("max") int limit);
}
