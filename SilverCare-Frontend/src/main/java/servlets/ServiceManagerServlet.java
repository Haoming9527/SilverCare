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
import models.Service;

import java.io.IOException;

@WebServlet("/manageService")
public class ServiceManagerServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/services";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        Client client = ClientBuilder.newClient();

        try {
            if ("add".equals(action)) {
                Service s = new Service();
                s.setServiceName(request.getParameter("serviceName"));
                s.setDescription(request.getParameter("description"));
                s.setPrice(Double.parseDouble(request.getParameter("price")));
                s.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                s.setImageUrl(request.getParameter("imageUrl"));
                
                Response apiResp = client.target(API_BASE_URL + "/add")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(s, MediaType.APPLICATION_JSON));
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("adminDashboard?success=add");
                } else {
                    response.sendRedirect("adminDashboard?error=add_failed");
                }
            } else if ("update".equals(action)) {
                Service s = new Service();
                s.setId(Integer.parseInt(request.getParameter("id")));
                s.setServiceName(request.getParameter("serviceName"));
                s.setDescription(request.getParameter("description"));
                s.setPrice(Double.parseDouble(request.getParameter("price")));
                s.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                s.setImageUrl(request.getParameter("imageUrl"));
                
                Response apiResp = client.target(API_BASE_URL + "/update")
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(s, MediaType.APPLICATION_JSON));

                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("adminDashboard?success=update");
                } else {
                    response.sendRedirect("adminDashboard?error=update_failed");
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Response apiResp = client.target(API_BASE_URL + "/delete/" + id)
                        .request().delete();
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("adminDashboard?success=delete");
                } else {
                    response.sendRedirect("adminDashboard?error=delete_failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=exception");
        } finally {
            client.close();
        }
    }
}
