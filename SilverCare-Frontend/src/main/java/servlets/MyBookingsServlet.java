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
import jakarta.ws.rs.core.Response; // Added this import
import utils.ApiConfig; // Added this import
import models.Booking;
import models.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/myBookings")
public class MyBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/bookings/user/";
    private static final String FEEDBACK_API_URL = ApiConfig.getBaseUrl() + "/feedback/user/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            // Fetch Bookings
            jakarta.ws.rs.core.Response bookingResponse = client.target(API_BASE_URL + user.getId())
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            List<Booking> bookings = null;
            if (bookingResponse.getStatus() == HttpServletResponse.SC_OK) {
                bookings = bookingResponse.readEntity(new GenericType<List<Booking>>() {});
                request.setAttribute("bookings", bookings);
            } 

            // Fetch Feedback
            if(bookings != null && !bookings.isEmpty()) {
                jakarta.ws.rs.core.Response feedbackResponse = client.target(FEEDBACK_API_URL + user.getId())
                        .request(MediaType.APPLICATION_JSON)
                        .get();
                
                if (feedbackResponse.getStatus() == HttpServletResponse.SC_OK) {
                   List<models.Feedback> feedbackList = feedbackResponse.readEntity(new GenericType<List<models.Feedback>>() {});
                   java.util.Map<Integer, models.Feedback> feedbackMap = new java.util.HashMap<>();
                   for(models.Feedback f : feedbackList) {
                       feedbackMap.put(f.getBookingId(), f);
                   }
                   request.setAttribute("feedbackMap", feedbackMap);
                }
            }

            request.getRequestDispatcher("bookings.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching bookings: " + e.getMessage());
        } finally {
            client.close();
        }
    }
}
