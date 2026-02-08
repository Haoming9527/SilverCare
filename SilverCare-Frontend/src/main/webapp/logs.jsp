<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<%@ page import="java.util.List" %>
<%@ page import="models.ActivityLog" %>
<!DOCTYPE html>
<html>
<head>
    <title>Activity Logs - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main class="container">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <div>
            <h1>Activity Logs</h1>
            <p>Track all administrative and user actions within the system.</p>
        </div>
        <div style="display: flex; gap: 10px;">
            <form action="logs" method="post">
                <input type="hidden" name="action" value="clear">
                <button type="submit" class="button button-danger" style="background: #e74c3c; color: white; border: none;">Clear All Logs</button>
            </form>
            <a href="adminDashboard" class="button button-secondary">Back to Dashboard</a>
        </div>
    </div>

    <% 
        String error = (String) request.getAttribute("error");
        if (error == null) error = request.getParameter("error");
        
        String success = request.getParameter("success");
        
        if (error != null) { 
    %>
        <div class="status-message status-error" style="display: block; margin-bottom: 20px;">
            <%= error %>
        </div>
    <% } %>

    <% if (success != null) { %>
        <div class="status-message status-success" style="display: block; margin-bottom: 20px;">
            <% if ("delete".equals(success)) { %>
                Log deleted successfully.
            <% } else if ("clear".equals(success)) { %>
                All logs cleared successfully.
            <% } %>
        </div>
    <% } %>

    <div class="logs-container">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>User</th>
                    <th>Action</th>
                    <th>Details</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<ActivityLog> logs = (List<ActivityLog>) request.getAttribute("logs");
                    if (logs == null || logs.isEmpty()) { 
                %>
                    <tr>
                        <td colspan="5" style="text-align: center;">No activity logs found.</td>
                    </tr>
                <% 
                    } else { 
                        for (ActivityLog log : logs) { 
                %>
                    <tr>
                        <td class="timestamp-cell"><%= log.getTimestamp() %></td>
                        <td>
                            <strong><%= log.getUsername() %></strong> 
                            (ID: <%= log.getUserId() > 0 ? log.getUserId() : "N/A" %>)
                        </td>
                        <td class="action-cell"><%= log.getAction() %></td>
                        <td><%= log.getDetails() != null ? log.getDetails() : "---" %></td>
                        <td>
                            <form action="logs" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= log.getId() %>">
                                <button type="submit" class="chip-button chip-button-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                        } 
                    } 
                %>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
