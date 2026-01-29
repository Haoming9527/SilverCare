package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Role;
import models.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null ||
            email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("login.jsp?errCode=invalid");
            return;
        }

        User loginRequest = new User();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Client client = ClientBuilder.newClient();
        String apiBaseUrl = "http://localhost:8081/api";
        
        try {
            // Authenticate user
            WebTarget userTarget = client.target(apiBaseUrl + "/users/login");
            Invocation.Builder userInvoker = userTarget.request(MediaType.APPLICATION_JSON);
            Response userResp = userInvoker.post(Entity.entity(loginRequest, MediaType.APPLICATION_JSON));
            
            if (userResp.getStatus() == Response.Status.OK.getStatusCode()) {
                User user = userResp.readEntity(User.class);
                
                //Fetch role details
                WebTarget roleTarget = client.target(apiBaseUrl + "/roles/getRole/" + user.getRole());
                Invocation.Builder roleInvoker = roleTarget.request(MediaType.APPLICATION_JSON);
                Response roleResp = roleInvoker.get();
                
                String roleName = "Customer"; // Fallback
                try {
                    if (roleResp.getStatus() == Response.Status.OK.getStatusCode()) {
                        Role role = roleResp.readEntity(Role.class);
                        if (role != null && role.getRoleName() != null) {
                            roleName = role.getRoleName();
                        }
                    }
                } catch (Exception re) {
                    re.printStackTrace();
                }
                
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("role", roleName); 
                session.setAttribute("userId", user.getId());
                session.setAttribute("sessUserID", user.getId()); 
                session.setAttribute("username", user.getUsername());

                if ("Admin".equals(roleName)) {
                    response.sendRedirect(request.getContextPath() + "/adminDashboard.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home.jsp");
                }
            } else {
                response.sendRedirect("login.jsp?errCode=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?errCode=invalid");
        }
    }
}