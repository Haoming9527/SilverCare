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
import models.Service;
import models.ServiceCategory;

import java.io.IOException;
import java.util.List;

@WebServlet("/editService")
public class EditServiceServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            Service service = client.target(API_BASE_URL + "/services/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Service.class);
                    
            List<ServiceCategory> categories = client.target(API_BASE_URL + "/serviceCategories/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ServiceCategory>>() {});
            
            request.setAttribute("service", service);
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("editService.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=load_edit_failed");
        } finally {
            client.close();
        }
    }
}
