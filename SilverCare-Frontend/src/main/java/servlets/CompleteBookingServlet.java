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
import models.User;

import java.io.IOException;

@WebServlet("/completeBooking")
public class CompleteBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = "http://localhost:8081/api/bookings";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr != null && !bookingIdStr.isEmpty()) {
            try {
                Client client = ClientBuilder.newClient();
                Response apiResponse = client.target(API_BASE_URL + "/" + bookingIdStr + "/status")
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity("Complete", MediaType.APPLICATION_JSON));

                if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                     boolean success = apiResponse.readEntity(Boolean.class);
                     if(success) {
                         response.sendRedirect("myBookings?success=completed");
                         return;
                     }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("myBookings?error=failed");
    }
}
