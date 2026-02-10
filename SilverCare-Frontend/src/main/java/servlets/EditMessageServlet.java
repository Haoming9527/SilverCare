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
import models.ContactMessage;
import java.io.IOException;

@WebServlet("/editMessage")
public class EditMessageServlet extends HttpServlet {
    private static final String API_BASE_URL = "http://localhost:8081/api/contact";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"Admin".equals(role)) {
            response.sendRedirect("login.jsp?errCode=unauthorized");
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect("adminDashboard?tab=messages&error=missing_id");
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            ContactMessage message = client.target(API_BASE_URL + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get(ContactMessage.class);

            if (message != null) {
                request.setAttribute("contactMessage", message);
                request.getRequestDispatcher("editMessage.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?tab=messages&error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard?tab=messages&error=exception");
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

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String messageBody = request.getParameter("message");

        ContactMessage updatedMessage = new ContactMessage();
        updatedMessage.setId(id);
        updatedMessage.setName(name);
        updatedMessage.setEmail(email);
        updatedMessage.setPhone(phone);
        updatedMessage.setSubject(subject);
        updatedMessage.setMessage(messageBody);

        Client client = ClientBuilder.newClient();
        try {
            Response apiResponse = client.target(API_BASE_URL + "/update")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(updatedMessage, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("adminDashboard?tab=messages&success=update_message");
            } else {
                response.sendRedirect("editMessage?id=" + id + "&error=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editMessage?id=" + id + "&error=exception");
        } finally {
            client.close();
        }
    }
}
