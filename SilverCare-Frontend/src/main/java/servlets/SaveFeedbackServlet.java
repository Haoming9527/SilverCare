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
import models.Feedback;
import models.User;

import java.io.IOException;

@WebServlet("/saveFeedback")
public class SaveFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/feedback/save";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            String feedbackIdStr = request.getParameter("feedbackId");
            
            Feedback feedback = new Feedback();
            feedback.setUserId(user.getId());
            feedback.setBookingId(bookingId);
            feedback.setRating(rating);
            feedback.setComment(comment);
            
            if (feedbackIdStr != null && !feedbackIdStr.isEmpty()) {
                feedback.setId(Integer.parseInt(feedbackIdStr));
            }

            Client client = ClientBuilder.newClient();
            Response apiResponse = client.target(API_BASE_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(feedback, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("myBookings?success=feedbackSaved");
            } else {
                response.sendRedirect("myBookings?error=feedbackFailed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myBookings?error=invalidInput");
        }
    }
}
