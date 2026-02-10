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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import models.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@WebServlet("/manageService")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ServiceManagerServlet extends HttpServlet {
    private static final String API_BASE_URL = ApiConfig.getBaseUrl() + "/services";

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
                
                String imageUrl = handleFileUpload(request);
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = "images/default.png";
                }
                s.setImageUrl(imageUrl);
                
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
                
                // Handle Image Upload (Optional for update)
                String imageUrl = handleFileUpload(request);
                if (imageUrl != null) {
                    s.setImageUrl(imageUrl);
                } else {
                    // Fetch existing image URL if no new file is uploaded
                    Service existing = client.target(API_BASE_URL + "/" + s.getId())
                            .request(MediaType.APPLICATION_JSON)
                            .get(Service.class);
                    String currentImg = existing.getImageUrl();
                    if (currentImg == null || currentImg.isEmpty()) {
                        currentImg = "images/default.png";
                    }
                    s.setImageUrl(currentImg);
                }
                
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

    private String handleFileUpload(HttpServletRequest request) throws ServletException, IOException {
        Part filePart = request.getPart("imageFile");
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + getFileName(filePart);
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        File file = new File(uploadDir, fileName);
        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }

        return "uploads/" + fileName;
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
