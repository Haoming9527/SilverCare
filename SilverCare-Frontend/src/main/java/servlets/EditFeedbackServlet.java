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
import java.io.IOException;

@WebServlet("/editFeedback")
public class EditFeedbackServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/feedback";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=feedback&error=missing_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            Feedback feedback = client.target(API_BASE_URL + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Feedback.class);

            if (feedback != null) {
                request.setAttribute("feedback", feedback);
                request.getRequestDispatcher("editFeedback.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?tab=feedback&error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=feedback&error=exception");
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
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setId(id);
        updatedFeedback.setUserId(userId);
        updatedFeedback.setRating(rating);
        updatedFeedback.setComment(comment);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_BASE_URL + "/update")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(updatedFeedback, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?tab=feedback&success=update_feedback");
            } else {
                response.sendRedirect("editFeedback?id=" + id + "&error=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editFeedback?id=" + id + "&error=exception");
        } finally {
            client.close();
        }
    }
}
