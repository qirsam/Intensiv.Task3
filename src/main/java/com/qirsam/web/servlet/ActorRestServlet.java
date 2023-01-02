package com.qirsam.web.servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qirsam.database.entity.Actor;
import com.qirsam.database.entity.Film;
import com.qirsam.service.ActorService;
import com.qirsam.utils.RestRequest;
import com.qirsam.utils.UrlPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;


@WebServlet(urlPatterns = UrlPath.ACTORS_V1 + "*")
public class ActorRestServlet extends HttpServlet {

    private static final ActorService actorService = ActorService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest restRequest = null;
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        try {
            restRequest = new RestRequest(pathInfo);
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
            out.println(e);
        }

        String json;
        if (pathInfo != null &&
                pathInfo.equals("/" + Objects.requireNonNull(restRequest).getId() + UrlPath.SUB_FILMS)) {
            List<Film> filmsByActorId = actorService.findFilmsByActorId(restRequest.getId());
            json = jsonMapper.writeValueAsString(filmsByActorId);
        } else if (restRequest.getId() != null) {
            Actor actorById = actorService.findById(restRequest.getId());
            json = jsonMapper.writeValueAsString(actorById);
        } else {
            List<Actor> allActors = actorService.findAll();
            json = jsonMapper.writeValueAsString(allActors);
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

        if (Objects.requireNonNull(restRequest).getId() == null) {
            try (BufferedReader reader = req.getReader()) {
                Actor actor = jsonMapper.readValue(reader, Actor.class);
                Actor savedActor = actorService.save(actor);
                resp.sendRedirect(UrlPath.ACTORS_V1 + savedActor.getId());
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

        if (Objects.requireNonNull(restRequest).getId() != null) {
            try(BufferedReader reader = req.getReader()) {
                Actor actor = jsonMapper.readValue(reader, Actor.class);
                actorService.update(actor);
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

        if (Objects.requireNonNull(restRequest).getId() != null) {
            actorService.delete(restRequest.getId());
            resp.sendRedirect(UrlPath.ACTORS_V1);
        } else {
            doGet(req, resp);
        }
    }
}
