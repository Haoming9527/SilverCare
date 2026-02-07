package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@WebServlet("/updateProfile")
public class UpdateProfileServlet extends HttpServlet {
    private static final String API_URL = "http://localhost:8081/api/users/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            response.sendRedirect("profile?error=invalidEmail");
            return;
        }
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        if (phone != null && !phone.trim().isEmpty()) {
            String trimmedPhone = phone.trim();
            if (!trimmedPhone.matches("\\d{8}")) {
                response.sendRedirect("profile?error=invalidPhone");
                return;
            }
            phone = "+65 " + trimmedPhone;
        }
        String address = request.getParameter("address");
        String healthInfo = request.getParameter("healthInfo");
        String preferences = request.getParameter("preferences");

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setAddress(address);
        user.setHealthInfo(healthInfo);
        user.setPreferences(preferences);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_URL + user.getId())
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(user, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                session.setAttribute("user", user);
                response.sendRedirect("profile?success=profileUpdated");
            } else {
                response.sendRedirect("profile?error=updateFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("profile?error=serverError");
        } finally {
            client.close();
        }
    }
}
