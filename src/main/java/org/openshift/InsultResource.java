package org.openshift;

import com.google.common.collect.ImmutableMap;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@RequestScoped
@Path("/insult")
public class InsultResource {

    private final InsultGenerator insultGenerator;

    public InsultResource() {
        this.insultGenerator = ServiceLocator.getInstance().insultGenerator();
    }

    @GET()
    @Produces("application/json")
    public Map<String, String> getInsult() {
        return ImmutableMap.of("insult", insultGenerator.generateInsult());
    }

}