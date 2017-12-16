package org.openshift;

import com.google.common.collect.ImmutableMap;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RequestScoped
@Path("/insult")
public class InsultController {

    private final InsultService insultService;

    public InsultController() {
        this.insultService = ServiceLocator.getInstance().insultService();
    }

    @GET()
    @Produces(APPLICATION_JSON)
    public Map<String, String> getInsult() {
        return ImmutableMap.of("insult", insultService.generateInsult());
    }

}