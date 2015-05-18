package com.singularity.archdesignhub.backend.content;

import com.singularity.archdesignhub.backend.endpoints.AgentEndpoint;
import com.singularity.archdesignhub.backend.endpoints.UploadUrlEndpoint;
import com.singularity.archdesignhub.backend.entities.Agent;

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
public class AgentsServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       handleRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);

    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Agent agent = new Agent();
        agent.setName(req.getParameter("name"));
        agent.setEmail(req.getParameter("email"));
        agent.setTel(req.getParameter("phone"));
        agent.setLocation(req.getParameter("location"));
        agent.setWebsite(req.getParameter("website"));
        agent.setTime(System.currentTimeMillis());

        Agent respAgent = new AgentEndpoint().insert(agent);

        JSONObject respObj = new JSONObject();
        respObj.put("agent_id", respAgent.getId());
        respObj.put("upload_images_url", new UploadUrlEndpoint().getUploadUrl(String.valueOf(respAgent.getId())));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(respObj.toJSONString());
        out.flush();
        out.close();
    }


}
