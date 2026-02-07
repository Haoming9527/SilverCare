package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@WebServlet("/deleteBooking")
public class DeleteBookingServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/bookings";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdStr = request.getParameter("bookingId");
        
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect("myBookings?error=missing_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            Response apiResponse = client.target(API_BASE_URL + "/delete/" + bookingId)
                    .request()
                    .delete();

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("myBookings?success=deleted");
            } else {
                response.sendRedirect("myBookings?error=delete_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myBookings?error=error");
        } finally {
            client.close();
        }
    }
}
