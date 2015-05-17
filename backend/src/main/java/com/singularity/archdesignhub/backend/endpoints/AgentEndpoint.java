package com.singularity.archdesignhub.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.singularity.archdesignhub.backend.entities.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Api(
        name = "agentApi",
        version = "v1",
        resource = "agent",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class AgentEndpoint {

    private static final Logger logger = Logger.getLogger(AgentEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Agent.class);
    }

    /**
     * Returns the {@link Agent} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Agent} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "agent/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Agent get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Agent with ID: " + id);
        Agent agent = ofy().load().type(Agent.class).id(id).now();
        if (agent == null) {
            throw new NotFoundException("Could not find Agent with ID: " + id);
        }
        return agent;
    }

    /**
     * Inserts a new {@code Agent}.
     */
    @ApiMethod(
            name = "insert",
            path = "agent",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Agent insert(Agent agent) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that agent.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(agent).now();
        logger.info("Created Agent with ID: " + agent.getId());

        return ofy().load().entity(agent).now();
    }

    /**
     * Updates an existing {@code Agent}.
     *
     * @param id    the ID of the entity to be updated
     * @param agent the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Agent}
     */
    @ApiMethod(
            name = "update",
            path = "agent/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Agent update(@Named("id") Long id, Agent agent) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(agent).now();
        logger.info("Updated Agent: " + agent);
        return ofy().load().entity(agent).now();
    }

    /**
     * Deletes the specified {@code Agent}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Agent}
     */
    @ApiMethod(
            name = "remove",
            path = "agent/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Agent.class).id(id).now();
        logger.info("Deleted Agent with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "agent",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Agent> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit, @Nullable @Named("oldest") Long oldest) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Agent> query = ofy().load().type(Agent.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }

        if (oldest != null)
            query.filter("time >", oldest);

        QueryResultIterator<Agent> queryIterator = query.iterator();
        List<Agent> agentList = new ArrayList<Agent>(limit);
        while (queryIterator.hasNext()) {
            agentList.add(queryIterator.next());
        }
        return CollectionResponse.<Agent>builder().setItems(agentList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Agent.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Agent with ID: " + id);
        }
    }
}