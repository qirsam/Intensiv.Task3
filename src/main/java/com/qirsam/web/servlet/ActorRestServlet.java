package com.qirsam.web.servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qirsam.service.ActorService;
import com.qirsam.utils.RestRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/api/v1/actors/*")
public class ActorRestServlet extends HttpServlet {

    private static final ActorService actorService = ActorService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest restRequest = new RestRequest(req.getPathInfo());

    }
}
