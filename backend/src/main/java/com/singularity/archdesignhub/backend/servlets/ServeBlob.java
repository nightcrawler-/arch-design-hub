package com.singularity.archdesignhub.backend.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServeBlob extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2970115435421698715L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));

		BlobInfoFactory blobInfoFactory = new BlobInfoFactory(
				DatastoreServiceFactory.getDatastoreService());
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
		resp.setContentLength(new Long(blobInfo.getSize()).intValue());
		resp.setHeader("content-type", blobInfo.getContentType());

		String contentDisposition = "";

		if (blobInfo.getFilename().endsWith(".jpg")) {
			resp.setHeader("content-type", "image/jpeg");
			contentDisposition = blobInfo.getFilename();

		} else if (blobInfo.getFilename().endsWith(".amt"))
			contentDisposition = blobInfo.getFilename().replace(",", "_")
					.replace(".amt", ".3gp");

		resp.setHeader("content-disposition", "attachment; filename="
				+ contentDisposition);

		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		blobstoreService.serve(blobKey, resp);

	}
}
