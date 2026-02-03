package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.ContactMessage;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_BASE_URL = "http://localhost:8081/api/contact/save";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("contact.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String messageContent = request.getParameter("message");

        if (name == null || email == null || subject == null || messageContent == null ||
            name.trim().isEmpty() || email.trim().isEmpty() ||
            subject.trim().isEmpty() || messageContent.trim().isEmpty()) {
            response.sendRedirect("contact?status=missing");
            return;
        }

        try {
            ContactMessage msg = new ContactMessage();
            msg.setName(name);
            msg.setEmail(email);
            msg.setPhone(phone);
            msg.setSubject(subject);
            msg.setMessage(messageContent);

            Client client = ClientBuilder.newClient();
            Response apiResponse = client.target(API_BASE_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(msg, MediaType.APPLICATION_JSON));

            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                response.sendRedirect("contact?status=success");
            } else {
                response.sendRedirect("contact?status=error");
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("contact?status=error");
        }
    }
}