package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@WebServlet("/manageUser")
public class ManageUserServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminRole = (String) session.getAttribute("role");

        if (!"Admin".equals(adminRole)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if ("delete".equals(action) && id != null) {
            Client client = ClientBuilder.newClient();
            try {
                Response apiResponse = client.target(API_BASE_URL + "/users/" + id)
                        .request()
                        .delete();

                if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("adminDashboard?tab=users&success=delete");
                } else {
                    response.sendRedirect("adminDashboard?tab=users&error=delete_failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("adminDashboard?tab=users&error=exception");
            } finally {
                client.close();
            }
        } else {
            response.sendRedirect("adminDashboard?tab=users");
        }
    }
}
