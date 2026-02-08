<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<%@ page import="java.util.List" %>
<%@ page import="models.Service" %>
<%@ page import="models.User" %>
<%@ page import="models.Booking" %>
<%@ page import="models.Feedback" %>
<%@ page import="models.ContactMessage" %>
<%@ page import="models.ServiceCategory" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .dashboard-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            border-bottom: 2px solid #eee;
        }
        .dashboard-tab {
            padding: 10px 20px;
            cursor: pointer;
            border: none;
            background: none;
            font-weight: bold;
            color: #666;
        }
        .dashboard-tab.active {
            color: #4b63d1;
            border-bottom: 3px solid #4b63d1;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        .data-table th, .data-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        .data-table th {
            background-color: #f4f4f4;
        }
        .chip-button {
            padding: 5px 10px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 0.85em;
            cursor: pointer;
            border: none;
        }
        .chip-button-primary { background: #4b63d1; color: white; }
        .chip-button-danger { background: #e74c3c; color: white; }
        .dashboard-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<%
    List<models.Service> services = (List<models.Service>) request.getAttribute("services");
    List<models.User> users = (List<models.User>) request.getAttribute("users");
    List<models.Feedback> feedbacks = (List<models.Feedback>) request.getAttribute("feedbacks");
    List<models.ContactMessage> messages = (List<models.ContactMessage>) request.getAttribute("messages");
    List<models.Booking> allBookings = (List<models.Booking>) request.getAttribute("bookings");
%>

<main class="container">
    <div class="dashboard-header">
        <div>
            <h1>Admin Dashboard</h1>
            <p>Welcome, Administrator</p>
            
            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <a href="adminAnalytics" class="button button-primary">View Analytics</a>
                <a href="adminCalendar" class="button button-primary" style="background: #2ecc71;">Booking Calendar</a>
            </div>
            
            <%-- Status Messages --%>
            <% 
                String successParam = request.getParameter("success");
                String errorParam = request.getParameter("error");
            %>
            
            <% if (successParam != null) { %>
                <div style="margin-top: 15px; padding: 10px 15px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 6px; font-size: 0.9rem;">
                    <% if ("checkin_success".equals(successParam)) { %>
                        <strong>Success!</strong> Caregiver checked in successfully.
                    <% } else if ("checkout_success".equals(successParam)) { %>
                        <strong>Success!</strong> Caregiver checked out successfully.
                    <% } else if ("update".equals(successParam)) { %>
                        <strong>Success!</strong> Record updated successfully.
                    <% } else if ("add".equals(successParam)) { %>
                        <strong>Success!</strong> Record added successfully.
                    <% } else if ("delete".equals(successParam)) { %>
                        <strong>Success!</strong> Record deleted successfully.
                    <% } else { %>
                        <strong>Success!</strong> Action completed successfully.
                    <% } %>
                </div>
            <% } %>
            
            <% if (errorParam != null) { %>
                <div style="margin-top: 15px; padding: 10px 15px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 6px; font-size: 0.9rem;">
                    <strong>Error!</strong> Failed to process the status update.
                </div>
            <% } %>
        </div>
        <div>
            <a href="addService" class="button button-primary">+ Add Service</a>
            <a href="manageCategory" class="button button-secondary">Manage Categories</a>
            <a href="logs" class="button button-secondary">Activity Logs</a>
        </div>
    </div>

    <%
        String activeTab = request.getParameter("tab");
        if (activeTab == null || activeTab.isEmpty()) activeTab = "services";
    %>

    <div class="dashboard-tabs">
        <a href="adminDashboard?tab=services" class="dashboard-tab <%= "services".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Services</a>
        <a href="adminDashboard?tab=categories" class="dashboard-tab <%= "categories".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Categories</a>
        <a href="adminDashboard?tab=users" class="dashboard-tab <%= "users".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Clients</a>
        <a href="adminDashboard?tab=feedback" class="dashboard-tab <%= "feedback".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Feedback</a>
        <a href="adminDashboard?tab=messages" class="dashboard-tab <%= "messages".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Messages</a>
        <a href="adminDashboard?tab=visits" class="dashboard-tab <%= "visits".equals(activeTab) ? "active" : "" %>" style="text-decoration: none;">Care Visits</a>
    </div>

    <!-- Services Tab -->
    <% if ("services".equals(activeTab)) { %>
    <div id="services-tab" class="tab-content active">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Service Name</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (services != null) { for (Service s : services) { %>
                    <tr>
                        <td><%= s.getId() %></td>
                        <td><%= s.getServiceName() %></td>
                        <td>$<%= String.format("%.2f", s.getPrice()) %></td>
                        <td>
                            <a href="editService?id=<%= s.getId() %>" class="chip-button chip-button-primary">Edit</a>
                            <form action="manageService" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= s.getId() %>">
                                <button type="submit" class="chip-button chip-button-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>

    <!-- Categories Tab -->
    <% if ("categories".equals(activeTab)) { %>
    <div id="categories-tab" class="tab-content active">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Category Name</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<models.ServiceCategory> allCategories = (List<models.ServiceCategory>) request.getAttribute("categories");
                    if (allCategories != null) { for (models.ServiceCategory cat : allCategories) { 
                %>
                    <tr>
                        <td><%= cat.getId() %></td>
                        <td><%= cat.getCategoryName() %></td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>

    <!-- Users Tab -->
    <% if ("users".equals(activeTab)) { %>
    <div id="users-tab" class="tab-content active">
        <div style="margin-bottom: 20px;">
            <a href="addUser.jsp" class="button button-primary">Add Client</a>
        </div>
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (users != null) { for (User u : users) { %>
                    <tr>
                        <td><%= u.getId() %></td>
                        <td><%= u.getUsername() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= u.getRole() == 1 ? "Admin" : "Customer" %></td>
                        <td>
                            <a href="editUser?id=<%= u.getId() %>" class="chip-button chip-button-primary">Edit</a>
                            <form action="manageUser" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= u.getId() %>">
                                <button type="submit" class="chip-button chip-button-danger" onclick="return confirm('Are you sure you want to delete this user?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>

    <!-- Feedback Tab -->
    <% if ("feedback".equals(activeTab)) { %>
    <div id="feedback-tab" class="tab-content active">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Service</th>
                    <th>Rating</th>
                    <th>Comment</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (feedbacks != null) { for (models.Feedback f : feedbacks) { %>
                    <tr>
                        <td><%= f.getId() %></td>
                        <td><%= f.getUsername() %></td>
                        <td><%= f.getServiceName() %></td>
                        <td><%= f.getRating() %>/5</td>
                        <td><%= f.getComment() %></td>
                        <td>
                            <form action="manageFeedback" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= f.getId() %>">
                                <button type="submit" class="chip-button chip-button-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>

    <!-- Messages Tab -->
    <% if ("messages".equals(activeTab)) { %>
    <div id="messages-tab" class="tab-content active">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Subject</th>
                    <th>Message</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (messages != null) { for (ContactMessage m : messages) { %>
                    <tr>
                        <td><%= m.getId() %></td>
                        <td><%= m.getName() %></td>
                        <td><%= m.getSubject() %></td>
                        <td><%= m.getMessage() %></td>
                        <td>
                            <form action="manageMessage" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= m.getId() %>">
                                <button type="submit" class="chip-button chip-button-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>

    <!-- Care Visits Tab -->
    <% if ("visits".equals(activeTab)) { %>
    <div id="visits-tab" class="tab-content active">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Client ID</th>
                    <th>Service</th>
                    <th>Caregiver</th>
                    <th>Scheduled Date</th>
                    <th>Status</th>
                    <th>Track Points</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (allBookings != null) { for (Booking b : allBookings) { %>
                    <tr>
                        <td><%= b.getId() %></td>
                        <td><%= b.getUserId() %></td>
                        <td><%= b.getServiceName() %></td>
                        <td><%= (b.getSpecificCaregiver() != null && !b.getSpecificCaregiver().isEmpty()) ? b.getSpecificCaregiver() : "Any" %></td>
                        <td><%= b.getScheduledDate() != null ? b.getScheduledDate().substring(0, 16).replace("T", " ") : "---" %></td>
                        <td>
                            <span class="status-badge status-<%= b.getStatus().toLowerCase().replace(" ", "-") %>">
                                <%= b.getStatus() %>
                            </span>
                        </td>
                        <td>
                            <div style="font-size: 0.85em;">
                                <div>In: <%= b.getCheckInTime() != null ? b.getCheckInTime().substring(0, 16).replace("T", " ") : "---" %></div>
                                <div>Out: <%= b.getCheckOutTime() != null ? b.getCheckOutTime().substring(0, 16).replace("T", " ") : "---" %></div>
                            </div>
                        </td>
                        <td>
                            <div style="display: flex; gap: 5px;">
                                <% if ("Pending".equalsIgnoreCase(b.getStatus()) && b.getCheckOutTime() == null) { %>
                                    <form action="handleCareVisit" method="post" style="display:inline;">
                                        <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                        <input type="hidden" name="action" value="checkin">
                                        <button type="submit" class="chip-button chip-button-primary">Check In</button>
                                    </form>
                                <% } else if ("In Progress".equalsIgnoreCase(b.getStatus())) { %>
                                    <form action="handleCareVisit" method="post" style="display:inline;">
                                        <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                        <input type="hidden" name="action" value="checkout">
                                        <button type="submit" class="chip-button" style="background: #27ae60; color: white;">Check Out</button>
                                    </form>
                                <% } %>
                            </div>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>
</main>

<jsp:include page="footer.html" />
</body>
</html>
