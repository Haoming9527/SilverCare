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

@WebServlet("/adminAnalytics")
public class AdminAnalyticsServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String serviceIdStr = request.getParameter("serviceId");
        Client client = ClientBuilder.newClient();

        try {
            List<RevenueTrend> revenueTrend = client.target(API_BASE_URL + "/bookings/revenue/monthly")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<RevenueTrend>>() {});

            List<ClientAnalytics> topClients = client.target(API_BASE_URL + "/bookings/analytics/top-clients")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ClientAnalytics>>() {});

            List<Service> services = client.target(API_BASE_URL + "/services/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Service>>() {});

            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                List<User> filteredUsers = client.target(API_BASE_URL + "/bookings/analytics/by-service/" + serviceIdStr)
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<User>>() {});
                request.setAttribute("filteredUsers", filteredUsers);
            }

            request.setAttribute("revenueTrend", revenueTrend);
            request.setAttribute("topClients", topClients);
            request.setAttribute("servicesList", services);
            request.setAttribute("selectedServiceId", serviceIdStr);

            request.getRequestDispatcher("adminAnalytics.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=analytics_load_failed");
        } finally {
            client.close();
        }
    }
}
