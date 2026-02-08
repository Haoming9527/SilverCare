package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (username == null || email == null || password == null ||
            username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("register.jsp?errCode=error");
            return;
        }

        User user = new User(username, email, password, 2); // Default role 2

        Client client = ClientBuilder.newClient();
        String restUrl = "http://localhost:8081/api/users/register";
        
        WebTarget target = client.target(restUrl);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        
        try {
            Response resp = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));
            
            if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                int rec = resp.readEntity(Integer.class);
                if (rec == 1) {
                    response.sendRedirect("register.jsp?errCode=success");
                } else if (rec == -1) {
                    response.sendRedirect("register.jsp?errCode=emailExists");
                } else {
                    response.sendRedirect("register.jsp?errCode=error");
                }
            } else {
                response.sendRedirect("register.jsp?errCode=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?errCode=error");
        }
    }
}