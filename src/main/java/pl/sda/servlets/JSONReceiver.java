package pl.sda.servlets;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import pl.sda.entities.User;
import pl.sda.pl.sda.storage.Storage;
import pl.sda.services.UserService;
import pl.sda.servlets.pl.sda.servlets.response.CreatUserResponse;
import pl.sda.servlets.pl.sda.servlets.response.DeleteUserResponse;
import pl.sda.servlets.pl.sda.servlets.response.UpdateUserResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class JSONReceiver extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();

        StringBuffer json = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        UserService userService = new UserService();
        CreatUserResponse response = userService.addUser(json.toString());

        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().write(mapper.writeValueAsString(response));

        System.out.println("");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getParameter("id");
        UserService userService = new UserService();
        User user = getUserByUUID(requestURI);

        ObjectMapper mapper = new ObjectMapper();

        resp.getWriter().write(mapper.writeValueAsString(user));
        resp.setContentType("application/json");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        UserService userService = new UserService();
        DeleteUserResponse result = userService.removeUserById(id);

        ObjectMapper mapper = new ObjectMapper();

        resp.getWriter().write(mapper.writeValueAsString(result));
        resp.setContentType("application/json");

    }

    public User getUserByUUID(String id) {
        User result = null;
        if (id != null && !id.isEmpty()) {
            UUID uuid = UUID.fromString(id);
            for (User user : Storage.getUsers()) {
                if (uuid.equals(user.getId())) {
                    result = user;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();

        StringBuffer json = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json.toString(), User.class);

        UserService userService = new UserService();
        UpdateUserResponse response = userService.updateUser(user);

        resp.getWriter().write(mapper.writeValueAsString(response));
    }
}
