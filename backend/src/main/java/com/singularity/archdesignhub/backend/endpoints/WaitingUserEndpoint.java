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
import com.singularity.archdesignhub.backend.entities.WaitingUser;

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
        name = "waitingUserApi",
        version = "v1",
        resource = "waitingUser",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class WaitingUserEndpoint {

    private static final Logger logger = Logger.getLogger(WaitingUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(WaitingUser.class);
    }

    /**
     * Returns the {@link WaitingUser} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code WaitingUser} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "waitingUser/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public WaitingUser get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting WaitingUser with ID: " + email);
        WaitingUser waitingUser = ofy().load().type(WaitingUser.class).id(email).now();
        if (waitingUser == null) {
            throw new NotFoundException("Could not find WaitingUser with ID: " + email);
        }
        return waitingUser;
    }

    /**
     * Inserts a new {@code WaitingUser}.
     */
    @ApiMethod(
            name = "insert",
            path = "waitingUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public WaitingUser insert(WaitingUser waitingUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that waitingUser.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(waitingUser).now();
        logger.info("Created WaitingUser with ID: " + waitingUser.getEmail());

        return ofy().load().entity(waitingUser).now();
    }

    /**
     * Updates an existing {@code WaitingUser}.
     *
     * @param email       the ID of the entity to be updated
     * @param waitingUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code WaitingUser}
     */
    @ApiMethod(
            name = "update",
            path = "waitingUser/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WaitingUser update(@Named("email") String email, WaitingUser waitingUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(waitingUser).now();
        logger.info("Updated WaitingUser: " + waitingUser);
        return ofy().load().entity(waitingUser).now();
    }

    /**
     * Deletes the specified {@code WaitingUser}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code WaitingUser}
     */
    @ApiMethod(
            name = "remove",
            path = "waitingUser/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(WaitingUser.class).id(email).now();
        logger.info("Deleted WaitingUser with ID: " + email);
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
            path = "waitingUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<WaitingUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<WaitingUser> query = ofy().load().type(WaitingUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<WaitingUser> queryIterator = query.iterator();
        List<WaitingUser> waitingUserList = new ArrayList<WaitingUser>(limit);
        while (queryIterator.hasNext()) {
            waitingUserList.add(queryIterator.next());
        }
        return CollectionResponse.<WaitingUser>builder().setItems(waitingUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(WaitingUser.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WaitingUser with ID: " + email);
        }
    }
}