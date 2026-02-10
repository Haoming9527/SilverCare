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
import jakarta.ws.rs.core.Response;
import utils.ApiConfig;
import models.Booking;

import java.io.IOException;
import java.util.List;

@WebServlet("/adminCalendar")
public class AdminCalendarServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            List<Booking> bookings = client.target(API_BASE_URL + "/bookings/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Booking>>() {});
            
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("adminCalendar.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=calendar_load_failed");
        } finally {
            client.close();
        }
    }
}
