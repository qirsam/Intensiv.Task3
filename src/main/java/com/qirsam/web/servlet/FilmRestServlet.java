package com.qirsam.web.servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qirsam.database.entity.Film;
import com.qirsam.service.FilmService;
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

@WebServlet(urlPatterns = UrlPath.FILMS_V1 + "*")
public class FilmRestServlet extends HttpServlet {

    private static final FilmService filmService = FilmService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(pathInfo);
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
            out.println(e);
        }

        String json;
        if (pathInfo != null && pathInfo.equals("/" + Objects.requireNonNull(restRequest).getId())){
            Film filmById = filmService.findById(restRequest.getId());
            json = jsonMapper.writeValueAsString(filmById);
        } else {
            List<Film> films = filmService.findAll();
            json = jsonMapper.writeValueAsString(films);
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
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if ((restRequest != null ? restRequest.getId() : null) == null) {
            try (BufferedReader reader = req.getReader()) {
                Film film = jsonMapper.readValue(reader, Film.class);
                Film savedFilm = filmService.save(film);
                resp.sendRedirect(UrlPath.FILMS_V1 + savedFilm.getId());
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
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if ((restRequest != null ? restRequest.getId() : null) != null) {
            try (BufferedReader reader = req.getReader()) {
                Film film = jsonMapper.readValue(reader, Film.class);
                filmService.update(film);
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
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if ((restRequest != null ? restRequest.getId() : null) != null) {
            filmService.delete(restRequest.getId());
            resp.sendRedirect(UrlPath.FILMS_V1);
        } else {
            doGet(req, resp);
        }
    }
}
