package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import models.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        Client client = ClientBuilder.newClient();

        try {
            List<User> users = client.target(API_BASE_URL + "/users/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<User>>() {});

            List<Service> services = client.target(API_BASE_URL + "/services/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Service>>() {});

            List<Feedback> feedbacks = client.target(API_BASE_URL + "/feedback/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Feedback>>() {});

            List<ContactMessage> messages = client.target(API_BASE_URL + "/contact/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ContactMessage>>() {});

            List<ServiceCategory> categories = client.target(API_BASE_URL + "/serviceCategories/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ServiceCategory>>() {});

            List<Booking> bookings = client.target(API_BASE_URL + "/bookings/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Booking>>() {});

            request.setAttribute("users", users);
            request.setAttribute("services", services);
            request.setAttribute("feedbacks", feedbacks);
            request.setAttribute("messages", messages);
            request.setAttribute("categories", categories);
            request.setAttribute("bookings", bookings);

            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=dashboard_load_failed");
        } finally {
            client.close();
        }
    }
}
