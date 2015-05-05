package com.singularity.archdesignhub.backend.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.singularity.archdesignhub.backend.endpoints.ImageEndpoint;
import com.singularity.archdesignhub.backend.entities.Image;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadBlob extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    BlobInfoFactory blobInfoFactory = new BlobInfoFactory(
            DatastoreServiceFactory.getDatastoreService());

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {


        Map<String, List<BlobKey>> blobs = BlobstoreServiceFactory
                .getBlobstoreService().getUploads(req);
        List<BlobKey> blobKeys = blobs.get("images");
        String commaSepKeys = "";

        for (BlobKey blobKey : blobKeys) {
            //do something
            if (blobKey != null) {
                updateImageInfo(req.getParameter("ownerId"), blobKey);
                commaSepKeys += blobKey.getKeyString() + (blobKeys.indexOf(blobKey) == blobKeys.size() - 1 ? "" : ",");
            }


        }

        if (commaSepKeys.length() < 2) {
            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.sendRedirect("/dashboard.html");
        }


    }


    private void updateImageInfo(String ownerId, BlobKey blobKey) {
        ImageEndpoint endpoint = new ImageEndpoint();
        BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
        Image image = new Image();
        image.setBlobKeyString(blobKey.getKeyString());
        image.setOwnerId(ownerId);
        image.setTime(System.currentTimeMillis());
        image.setServingUrl(ImagesServiceFactory.getImagesService()
                .getServingUrl(blobKey));
        image.setName(blobInfo.getFilename());
        endpoint.insert(image);
    }


}
