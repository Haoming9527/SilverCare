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
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


import models.Booking;
import models.User;

import java.io.IOException;
import java.io.StringReader;

@WebServlet("/saveBooking")
public class SaveBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = "http://localhost:8081/api/bookings/create";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String serviceIdStr = request.getParameter("service_id");
        String scheduledDate = request.getParameter("scheduled_date");
        String scheduledTime = request.getParameter("scheduled_time");
        String specificCaregiver = request.getParameter("specific_caregiver");
        String specialRequest = request.getParameter("special_request");
     
        try {
            Booking booking = new Booking();
            booking.setUserId(user.getId());
            booking.setServiceId(Integer.parseInt(serviceIdStr));
            

            booking.setScheduledDate(scheduledDate + " " + scheduledTime + ":00");
            
            booking.setSpecificCaregiver(specificCaregiver);
            booking.setSpecialRequest(specialRequest);

            Client client = ClientBuilder.newClient();
            Response apiResponse = client.target(API_BASE_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(booking, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonResponse = apiResponse.readEntity(String.class);
                JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse));
                JsonObject object = jsonReader.readObject();
                String paymentUrl = object.getString("paymentUrl");
                
                if (paymentUrl != null && !paymentUrl.isEmpty()) {
                    response.sendRedirect(paymentUrl);
                } else {
                    response.sendRedirect("bookingForm.jsp?error=no_payment_url&service_id=" + serviceIdStr);
                }
            } else {
                response.sendRedirect("bookingForm.jsp?error=api_error&service_id=" + serviceIdStr);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("bookingForm.jsp?error=exception&service_id=" + serviceIdStr);
        }
    }
}
