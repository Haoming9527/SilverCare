package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import models.User;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminRole = (String) session.getAttribute("role");

        if (!"Admin".equals(adminRole)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int role = Integer.parseInt(request.getParameter("role"));

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_BASE_URL + "/users/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newUser, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode() || apiResponse.getStatus() == 200) {
                int result = apiResponse.readEntity(Integer.class);
                if (result > 0) {
                    response.sendRedirect("adminDashboard?tab=users&success=add");
                } else if (result == -1) {
                    response.sendRedirect("addUser.jsp?error=email_exists");
                } else {
                    response.sendRedirect("addUser.jsp?error=failed");
                }
            } else {
                response.sendRedirect("addUser.jsp?error=api_error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addUser.jsp?error=exception");
        } finally {
            client.close();
        }
    }
}
