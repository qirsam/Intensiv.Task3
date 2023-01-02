package com.qirsam.web.servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qirsam.database.entity.Studio;
import com.qirsam.service.StudioService;
import com.qirsam.utils.RestRequest;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/api/v1/studios/*")
public class StudioRestServlet extends HttpServlet {

    private final StudioService studioService = StudioService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter out = resp.getWriter();
        RestRequest restRequest = null;

        try {
            restRequest = new RestRequest(pathInfo);
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
            out.println(e);
        }

        String json;
        if ((restRequest != null ? restRequest.getId() : null) != null) {
            Studio studioById = studioService.findById(restRequest.getId());
            json = jsonMapper.writeValueAsString(studioById);
        } else {
            List<Studio> allStudios = studioService.findAll();
            json = jsonMapper.writeValueAsString(allStudios);
        }
        if (!json.equals("null")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.write(json);
        } else {
            resp.setStatus(404);
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (restRequest.getId() == null) {
            try (BufferedReader reader = req.getReader()) {
                Studio studio = jsonMapper.readValue(reader, Studio.class);
                Studio savedStudio = studioService.save(studio);
                resp.sendRedirect("/api/v1/studios/" + savedStudio.getId());
            }
        } else {
            resp.setStatus(201);
            doGet(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (restRequest.getId() != null) {
            try (BufferedReader reader = req.getReader()) {
                Studio studio = jsonMapper.readValue(reader, Studio.class);
                studioService.update(studio);
                doGet(req, resp);
            }
        } else {
            resp.setStatus(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (restRequest.getId() != null) {
            studioService.delete(restRequest.getId());
            resp.sendRedirect("/api/v1/studios");
        } else {
            doGet(req, resp);
        }
    }
}
