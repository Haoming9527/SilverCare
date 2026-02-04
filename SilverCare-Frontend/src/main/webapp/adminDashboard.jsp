<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Service" %>
<%@ page import="models.User" %>
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
%>

<main class="container">
    <div class="dashboard-header">
        <div>
            <h1>Admin Dashboard</h1>
            <p>Welcome, Administrator</p>
        </div>
        <div>
            <a href="addService" class="button button-primary">+ Add Service</a>
            <a href="manageCategory" class="button button-secondary">Manage Categories</a>
        </div>
    </div>

    <div class="dashboard-tabs">
        <button class="dashboard-tab active" onclick="showTab('services', this)">Services</button>
        <button class="dashboard-tab" onclick="showTab('categories', this)">Categories</button>
        <button class="dashboard-tab" onclick="showTab('users', this)">Clients</button>
        <button class="dashboard-tab" onclick="showTab('feedback', this)">Feedback</button>
        <button class="dashboard-tab" onclick="showTab('messages', this)">Messages</button>
    </div>

    <!-- Services Tab -->
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
                            <form action="manageService" method="post" style="display:inline;" onsubmit="return confirm('Delete this service?')">
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

    <!-- Categories Tab -->
    <div id="categories-tab" class="tab-content">
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

    <!-- Users Tab -->
    <div id="users-tab" class="tab-content">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                <% if (users != null) { for (User u : users) { %>
                    <tr>
                        <td><%= u.getId() %></td>
                        <td><%= u.getUsername() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= u.getRole() == 1 ? "Admin" : "Customer" %></td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>

    <!-- Feedback Tab -->
    <div id="feedback-tab" class="tab-content">
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
                            <form action="manageFeedback" method="post" style="display:inline;" onsubmit="return confirm('Delete this feedback?')">
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

    <!-- Messages Tab -->
    <div id="messages-tab" class="tab-content">
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
                            <form action="manageMessage" method="post" style="display:inline;" onsubmit="return confirm('Delete this message?')">
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
</main>

<script>
    function showTab(tabName, element) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.dashboard-tab').forEach(tab => tab.classList.remove('active'));
        document.getElementById(tabName + '-tab').classList.add('active');
        element.classList.add('active');
    }
</script>

<jsp:include page="footer.html" />
</body>
</html>
