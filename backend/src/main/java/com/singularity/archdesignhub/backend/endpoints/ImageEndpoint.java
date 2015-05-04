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
import com.singularity.archdesignhub.backend.entities.Image;

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
        name = "imageApi",
        version = "v1",
        resource = "image",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class ImageEndpoint {

    private static final Logger logger = Logger.getLogger(ImageEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Image.class);
    }

    /**
     * Returns the {@link Image} with the corresponding ID.
     *
     * @param blobKeyString the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Image} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "image/{blobKeyString}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Image get(@Named("blobKeyString") String blobKeyString) throws NotFoundException {
        logger.info("Getting Image with ID: " + blobKeyString);
        Image image = ofy().load().type(Image.class).id(blobKeyString).now();
        if (image == null) {
            throw new NotFoundException("Could not find Image with ID: " + blobKeyString);
        }
        return image;
    }

    /**
     * Inserts a new {@code Image}.
     */
    @ApiMethod(
            name = "insert",
            path = "image",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Image insert(Image image) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that image.blobKeyString has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(image).now();
        logger.info("Created Image with ID: " + image.getBlobKeyString());

        return ofy().load().entity(image).now();
    }

    /**
     * Updates an existing {@code Image}.
     *
     * @param blobKeyString the ID of the entity to be updated
     * @param image         the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code blobKeyString} does not correspond to an existing
     *                           {@code Image}
     */
    @ApiMethod(
            name = "update",
            path = "image/{blobKeyString}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Image update(@Named("blobKeyString") String blobKeyString, Image image) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(blobKeyString);
        ofy().save().entity(image).now();
        logger.info("Updated Image: " + image);
        return ofy().load().entity(image).now();
    }

    /**
     * Deletes the specified {@code Image}.
     *
     * @param blobKeyString the ID of the entity to delete
     * @throws NotFoundException if the {@code blobKeyString} does not correspond to an existing
     *                           {@code Image}
     */
    @ApiMethod(
            name = "remove",
            path = "image/{blobKeyString}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("blobKeyString") String blobKeyString) throws NotFoundException {
        checkExists(blobKeyString);
        ofy().delete().type(Image.class).id(blobKeyString).now();
        logger.info("Deleted Image with ID: " + blobKeyString);
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
            path = "image",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Image> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Image> query = ofy().load().type(Image.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Image> queryIterator = query.iterator();
        List<Image> imageList = new ArrayList<Image>(limit);
        while (queryIterator.hasNext()) {
            imageList.add(queryIterator.next());
        }
        return CollectionResponse.<Image>builder().setItems(imageList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String blobKeyString) throws NotFoundException {
        try {
            ofy().load().type(Image.class).id(blobKeyString).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Image with ID: " + blobKeyString);
        }
    }
}