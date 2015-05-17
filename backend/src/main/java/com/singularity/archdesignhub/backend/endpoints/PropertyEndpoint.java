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
import com.singularity.archdesignhub.backend.entities.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "propertyApi",
        version = "v1",
        resource = "property",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class PropertyEndpoint {

    private static final Logger logger = Logger.getLogger(PropertyEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Property.class);
    }

    /**
     * Returns the {@link Property} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Property} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "property/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Property get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Property with ID: " + id);
        Property property = ofy().load().type(Property.class).id(id).now();
        if (property == null) {
            throw new NotFoundException("Could not find Property with ID: " + id);
        }
        return property;
    }

    /**
     * Inserts a new {@code Property}.
     */
    @ApiMethod(
            name = "insert",
            path = "property",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Property insert(Property property) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that property.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(property).now();
        logger.info("Created Property with ID: " + property.getId());

        return ofy().load().entity(property).now();
    }

    /**
     * Updates an existing {@code Property}.
     *
     * @param id       the ID of the entity to be updated
     * @param property the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Property}
     */
    @ApiMethod(
            name = "update",
            path = "property/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Property update(@Named("id") Long id, Property property) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(property).now();
        logger.info("Updated Property: " + property);
        return ofy().load().entity(property).now();
    }

    /**
     * Deletes the specified {@code Property}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Property}
     */
    @ApiMethod(
            name = "remove",
            path = "property/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Property.class).id(id).now();
        logger.info("Deleted Property with ID: " + id);
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
            path = "property",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Property> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit,
                                             @Nullable @Named("oldest") Long oldest) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Property> query = ofy().load().type(Property.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        if (oldest != null)
            query.filter("time >", oldest);

        QueryResultIterator<Property> queryIterator = query.iterator();
        List<Property> propertyList = new ArrayList<Property>(limit);
        while (queryIterator.hasNext()) {
            propertyList.add(queryIterator.next());
        }
        return CollectionResponse.<Property>builder().setItems(propertyList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Property.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Property with ID: " + id);
        }
    }
}