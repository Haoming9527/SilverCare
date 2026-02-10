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
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import models.Role;
import models.User;
import utils.ApiConfig;

@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminRole = (String) session.getAttribute("role");

        if (!"Admin".equals(adminRole)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=users&error=missing_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            User user = client.target(API_BASE_URL + "/users/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(User.class);

            if (user != null) {
                request.setAttribute("editUser", user);
                request.getRequestDispatcher("editUser.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?tab=users&error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=users&error=exception");
        } finally {
            client.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminRole = (String) session.getAttribute("role");

        if (!"Admin".equals(adminRole)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int role = Integer.parseInt(request.getParameter("role"));
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String postalCode = request.getParameter("postalCode");
        String healthInfo = request.getParameter("healthInfo");

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setUsername(username);
        updatedUser.setEmail(email);
        updatedUser.setPassword(password); 
        updatedUser.setRole(role);
        updatedUser.setPhone(phone);
        updatedUser.setAddress(address);
        updatedUser.setPostalCode(postalCode);
        updatedUser.setHealthInfo(healthInfo);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_BASE_URL + "/users/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(updatedUser, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?tab=users&success=update");
            } else {
                response.sendRedirect("editUser?id=" + id + "&error=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editUser?id=" + id + "&error=exception");
        } finally {
            client.close();
        }
    }
}
