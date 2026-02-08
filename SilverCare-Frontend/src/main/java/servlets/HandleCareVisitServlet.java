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
import java.io.IOException;

@WebServlet("/handleCareVisit")
public class HandleCareVisitServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/bookings";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String bookingIdStr = request.getParameter("bookingId");
        String action = request.getParameter("action"); // "checkin" or "checkout"

        if (bookingIdStr == null || action == null) {
            response.sendRedirect("adminDashboard?error=invalid_request");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            String url = API_BASE_URL + "/" + bookingIdStr + "/" + action;
            Response apiResponse = client.target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity("", MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?success=" + action + "_success");
            } else {
                response.sendRedirect("adminDashboard?error=" + action + "_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=error");
        } finally {
            client.close();
        }
    }
}
