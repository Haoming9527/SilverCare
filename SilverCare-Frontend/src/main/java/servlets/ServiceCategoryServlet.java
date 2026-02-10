package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Service;
import models.ServiceCategory;
import utils.ApiConfig;

import java.io.IOException;
import java.util.List;

@WebServlet("/serviceCategories")
public class ServiceCategoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = ApiConfig.getBaseUrl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchParam = request.getParameter("search");
        String categoryIdParam = request.getParameter("category_id");
        Client client = ClientBuilder.newClient();

        try {
            // Fetch All Categories
            List<ServiceCategory> categories = client.target(API_BASE_URL + "/serviceCategories/all")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ServiceCategory>>() {});

            // Fetch Services based on category or search filter
            List<Service> services;
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                services = client.target(API_BASE_URL + "/services/search/" + java.net.URLEncoder.encode(searchParam, "UTF-8"))
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Service>>() {});
            } else if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
                services = client.target(API_BASE_URL + "/services/category/" + categoryIdParam)
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Service>>() {});
            } else {
                services = client.target(API_BASE_URL + "/services/all")
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Service>>() {});
            }

            request.setAttribute("categories", categories);
            request.setAttribute("services", services);
            request.setAttribute("selectedCategoryId", categoryIdParam);
            request.setAttribute("searchQuery", searchParam);

            request.getRequestDispatcher("serviceCategory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data from backend");
        } finally {
            client.close();
        }
    }
}
