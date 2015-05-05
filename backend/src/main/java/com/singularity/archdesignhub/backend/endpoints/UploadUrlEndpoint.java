package com.singularity.archdesignhub.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.singularity.archdesignhub.backend.entities.UploadUrl;

import java.io.UnsupportedEncodingException;

import javax.inject.Named;

/**
 * Created by Frederick on 5/4/2015.
 */

@Api(
        name = "uploadUrlApi",
        version = "v1",
        resource = "uploadUrl",
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.archdesignhub.singularity.com",
                ownerName = "entities.backend.archdesignhub.singularity.com",
                packagePath = ""
        )
)
public class UploadUrlEndpoint {

    @ApiMethod(
            name = "get",
            path = "uploadUrl/{ownerId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public UploadUrl getUploadUrl(@Named("ownerId") String ownerId
    ) throws UnsupportedEncodingException {
        UploadUrl url = new UploadUrl();
        url.setUrl(BlobstoreServiceFactory.getBlobstoreService().createUploadUrl(
                "/uploadBlob?ownerId=" + ownerId));
        return url;

    }
}
