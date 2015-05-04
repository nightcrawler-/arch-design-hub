package com.singularity.archdesignhub.backend.servlets;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUploadUrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -499716699003345090L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String ownerId = req.getParameter("ownerId");

		String blobUploadUrl = BlobstoreServiceFactory.getBlobstoreService()
				.createUploadUrl(
						"/uploadBlob?clientId=" + ownerId);
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();
		out.print(blobUploadUrl);
		out.flush();
		out.close();

	}

}
