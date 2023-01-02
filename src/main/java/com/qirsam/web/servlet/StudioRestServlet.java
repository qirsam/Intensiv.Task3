package com.qirsam.web.servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qirsam.database.entity.Studio;
import com.qirsam.service.StudioService;

import javax.servlet.ServletException;
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

    private class RestRequest {
        private Pattern regExIdPattern = Pattern.compile("/([0-9]*)");
        private Long id;

        public RestRequest(String pathInfo) throws ServletException {
            if (pathInfo.equals("/")) {
                return;
            }

            Matcher matcher = regExIdPattern.matcher(pathInfo);
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
                return;
            }

            throw new ServletException("Invalid URI");
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

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
        if (restRequest.getId() != null) {
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
        try (BufferedReader reader = req.getReader()) {
            Studio studio = jsonMapper.readValue(reader, Studio.class);
            System.out.println(studio);
            Studio savedStudio = studioService.save(studio);
            resp.setStatus(201);
            resp.sendRedirect(req.getContextPath() + "/api/v1/studios/" + savedStudio.getId());
        }
    }
}
