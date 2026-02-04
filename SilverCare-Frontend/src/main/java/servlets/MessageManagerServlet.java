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

@WebServlet("/manageMessage")
public class MessageManagerServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/contact";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Client client = ClientBuilder.newClient();
            try {
                Response apiResp = client.target(API_BASE_URL + "/delete/" + id)
                        .request().delete();
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("adminDashboard?success=delete_message");
                } else {
                    response.sendRedirect("adminDashboard?error=delete_message_failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("adminDashboard?error=exception");
            } finally {
                client.close();
            }
        } else {
            response.sendRedirect("adminDashboard");
        }
    }
}
