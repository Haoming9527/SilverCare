<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.ContactMessage" %>
<%
    ContactMessage message = (ContactMessage) request.getAttribute("contactMessage");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Message - SilverCare Admin</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .edit-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }
        .form-header {
            margin-bottom: 25px;
            border-bottom: 2px solid var(--color-primary-light);
            padding-bottom: 15px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: var(--color-primary-dark);
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container">
        <div class="edit-container">
            <div class="form-header">
                <h2>Edit Contact Message</h2>
            </div>

            <% if (message != null) { %>
                <form action="editMessage" method="post">
                    <input type="hidden" name="id" value="<%= message.getId() %>">
                    
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" value="<%= message.getName() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="<%= message.getEmail() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="text" id="phone" name="phone" value="<%= message.getPhone() != null ? message.getPhone() : "" %>">
                    </div>

                    <div class="form-group">
                        <label for="subject">Subject</label>
                        <input type="text" id="subject" name="subject" value="<%= message.getSubject() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="message">Message</label>
                        <textarea id="message" name="message" rows="6" required><%= message.getMessage() %></textarea>
                    </div>

                    <div style="display: flex; gap: 15px; margin-top: 30px;">
                        <button type="submit" class="button button-primary" style="flex: 1;">Save Changes</button>
                        <a href="adminDashboard?tab=messages" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                    </div>
                </form>
            <% } else { %>
                <div class="status-message status-error">
                    Message not found.
                </div>
                <a href="adminDashboard?tab=messages" class="button button-primary">Return to Dashboard</a>
            <% } %>
        </div>
    </div>

    <%@ include file="footer.html" %>
</body>
</html>
