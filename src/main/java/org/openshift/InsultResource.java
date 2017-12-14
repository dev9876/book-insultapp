package org.openshift;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("/insult")
public class InsultResource {

    private final InsultGenerator insultGenerator;

    public InsultResource() {
        this.insultGenerator = new InsultGenerator();
    }

    @GET()
    @Produces("application/json")
    public Map<String, String> getInsult() {
        final Map<String, String> theInsult = new HashMap<>();
        theInsult.put("insult", insultGenerator.generateInsult());
        return theInsult;
    }

}