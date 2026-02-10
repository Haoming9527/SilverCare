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
import utils.ApiConfig;
import models.Booking;
import java.io.IOException;

@WebServlet("/editCareVisit")
public class EditCareVisitServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/bookings";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=visits&error=missing_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            Booking booking = client.target(API_BASE_URL + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Booking.class);

            if (booking != null) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("editCareVisit.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?tab=visits&error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=visits&error=exception");
        } finally {
            client.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String scheduledDate = request.getParameter("scheduledDate");
        String caregiver = request.getParameter("caregiver");
        String specialRequest = request.getParameter("specialRequest");
        String status = request.getParameter("status");

        Booking updatedBooking = new Booking();
        updatedBooking.setId(id);
        updatedBooking.setUserId(userId);
        updatedBooking.setScheduledDate(scheduledDate);
        updatedBooking.setSpecificCaregiver(caregiver);
        updatedBooking.setSpecialRequest(specialRequest);
        updatedBooking.setStatus(status);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_BASE_URL + "/update")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(updatedBooking, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?tab=visits&success=update_booking");
            } else {
                response.sendRedirect("editCareVisit?id=" + id + "&error=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editCareVisit?id=" + id + "&error=exception");
        } finally {
            client.close();
        }
    }
}
