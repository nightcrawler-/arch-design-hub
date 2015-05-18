package com.singularity.archdesignhub.backend.content;

import com.singularity.archdesignhub.backend.endpoints.PropertyEndpoint;
import com.singularity.archdesignhub.backend.endpoints.UploadUrlEndpoint;
import com.singularity.archdesignhub.backend.entities.Property;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Frederick on 5/18/2015.
 */
public class PropertiesServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Property property = new Property();
        property.setName(req.getParameter("title"));
        property.setTime(System.currentTimeMillis());
        property.setValue(Integer.parseInt(req.getParameter("value")));
        property.setAgentId(req.getParameter("agent_id"));
        property.setLocation(req.getParameter("location"));
        property.setBathrooms(Integer.parseInt(req.getParameter("bathrooms")));
        property.setBedrooms(Integer.parseInt(req.getParameter("bedrooms")));
        property.setIntent(req.getParameter("intent"));
        property.setDescription(req.getParameter("description"));
        property.setLatt(Float.parseFloat(req.getParameter("latt")));
        property.setLongi(Float.parseFloat(req.getParameter("longi")));

        Property respProperty = new PropertyEndpoint().insert(property);

        JSONObject respObj = new JSONObject();
        respObj.put("property_id", respProperty.getId());
        respObj.put("upload_images_url", new UploadUrlEndpoint().getUploadUrl(String.valueOf(respProperty.getId())));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(respObj.toJSONString());
        out.flush();
        out.close();

    }
}
