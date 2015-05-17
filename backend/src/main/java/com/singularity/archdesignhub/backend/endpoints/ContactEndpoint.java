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
import com.singularity.archdesignhub.backend.entities.Contact;

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
        name = "contactApi",
        version = "v1",
        resource = "contact",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class ContactEndpoint {

    private static final Logger logger = Logger.getLogger(ContactEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Contact.class);
    }

    /**
     * Returns the {@link Contact} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Contact} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "contact/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Contact get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Contact with ID: " + id);
        Contact contact = ofy().load().type(Contact.class).id(id).now();
        if (contact == null) {
            throw new NotFoundException("Could not find Contact with ID: " + id);
        }
        return contact;
    }

    /**
     * Inserts a new {@code Contact}.
     */
    @ApiMethod(
            name = "insert",
            path = "contact",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Contact insert(Contact contact) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that contact.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(contact).now();
        logger.info("Created Contact with ID: " + contact.getId());

        return ofy().load().entity(contact).now();
    }

    /**
     * Updates an existing {@code Contact}.
     *
     * @param id      the ID of the entity to be updated
     * @param contact the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Contact}
     */
    @ApiMethod(
            name = "update",
            path = "contact/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Contact update(@Named("id") Long id, Contact contact) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(contact).now();
        logger.info("Updated Contact: " + contact);
        return ofy().load().entity(contact).now();
    }

    /**
     * Deletes the specified {@code Contact}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Contact}
     */
    @ApiMethod(
            name = "remove",
            path = "contact/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Contact.class).id(id).now();
        logger.info("Deleted Contact with ID: " + id);
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
            path = "contact",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Contact> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Contact> query = ofy().load().type(Contact.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Contact> queryIterator = query.iterator();
        List<Contact> contactList = new ArrayList<Contact>(limit);
        while (queryIterator.hasNext()) {
            contactList.add(queryIterator.next());
        }
        return CollectionResponse.<Contact>builder().setItems(contactList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Contact.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Contact with ID: " + id);
        }
    }
}