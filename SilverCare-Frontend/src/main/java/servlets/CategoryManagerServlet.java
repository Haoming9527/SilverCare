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
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import utils.ApiConfig;
import models.ServiceCategory;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageCategory")
public class CategoryManagerServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/serviceCategories";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            List<ServiceCategory> categories = client.target(API_BASE_URL + "/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ServiceCategory>>() {});
            
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?error=load_categories_failed");
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

        String action = request.getParameter("action");
        Client client = ClientBuilder.newClient();

        try {
            if ("add".equals(action)) {
                String name = request.getParameter("categoryName");
                ServiceCategory sc = new ServiceCategory();
                sc.setCategoryName(name);
                
                Response apiResp = client.target(API_BASE_URL + "/add")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(sc, MediaType.APPLICATION_JSON));
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("manageCategory?success=add");
                } else {
                    response.sendRedirect("manageCategory?error=add_failed");
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Response apiResp = client.target(API_BASE_URL + "/delete/" + id)
                        .request().delete();
                
                if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                    response.sendRedirect("manageCategory?success=delete");
                } else {
                    response.sendRedirect("manageCategory?error=delete_failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manageCategory?error=exception");
        } finally {
            client.close();
        }
    }
}
