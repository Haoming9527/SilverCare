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
import models.ActivityLog;

import java.io.IOException;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

@WebServlet("/logs")
public class LogsServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/logs";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ... (rest of doGet)
        HttpSession session = request.getSession();
        
        Object roleObj = session.getAttribute("role");
        boolean isAdmin = false;
        
        if (roleObj instanceof String) {
            isAdmin = "Admin".equalsIgnoreCase((String) roleObj);
        } else if (roleObj instanceof Integer) {
            isAdmin = (Integer) roleObj == 1;
        }

        if (!isAdmin) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            List<ActivityLog> logs = client.target(API_BASE_URL + "/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ActivityLog>>() {});
            
            request.setAttribute("logs", logs);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to fetch logs: " + e.getMessage());
        } finally {
            client.close();
        }

        request.getRequestDispatcher("logs.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        Object roleObj = session.getAttribute("role");
        boolean isAdmin = false;
        
        if (roleObj instanceof String) {
            isAdmin = "Admin".equalsIgnoreCase((String) roleObj);
        } else if (roleObj instanceof Integer) {
            isAdmin = (Integer) roleObj == 1;
        }

        if (!isAdmin) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        Client client = ClientBuilder.newClient();

        try {
            if ("delete".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    Response apiResp = client.target(API_BASE_URL + "/delete/" + idStr)
                            .request().delete();
                    
                    if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                        response.sendRedirect("logs?success=delete");
                    } else {
                        response.sendRedirect("logs?error=delete_failed");
                    }
                }
            } else if ("clear".equals(action)) {
                Response apiResp = client.target(API_BASE_URL + "/clear")
                        .request().delete();
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("logs?success=clear");
                } else {
                    response.sendRedirect("logs?error=clear_failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("logs?error=exception");
        } finally {
            client.close();
        }
    }
}
