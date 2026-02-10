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
import models.ServiceCategory;

import java.io.IOException;

@WebServlet("/editCategoryManager")
public class EditCategoryManagerServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/serviceCategories";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=categories&error=invalid_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            int id = Integer.parseInt(idParam);
            ServiceCategory category = client.target(API_BASE_URL + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(ServiceCategory.class);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("editCategoryDetail.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?tab=categories&error=category_not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=categories&error=load_failed");
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

        String idParam = request.getParameter("id");
        String name = request.getParameter("categoryName");

        if (idParam == null || idParam.isEmpty() || name == null || name.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=categories&error=missing_info");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            int id = Integer.parseInt(idParam);
            ServiceCategory sc = new ServiceCategory();
            sc.setId(id);
            sc.setCategoryName(name);

            Response apiResp = client.target(API_BASE_URL + "/update")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(sc, MediaType.APPLICATION_JSON));

            if (apiResp.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?tab=categories&success=update");
            } else {
                response.sendRedirect("adminDashboard?tab=categories&error=update_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=categories&error=exception");
        } finally {
            client.close();
        }
    }
}
