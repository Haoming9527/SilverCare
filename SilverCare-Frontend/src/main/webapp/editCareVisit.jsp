<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Booking" %>
<%
    Booking booking = (Booking) request.getAttribute("booking");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Care Visit - SilverCare Admin</title>
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
        .form-group input, .form-group select, .form-group textarea {
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
                <h2>Edit Care Visit</h2>
            </div>

            <% if (booking != null) { %>
                <form action="editCareVisit" method="post">
                    <input type="hidden" name="id" value="<%= booking.getId() %>">
                    <input type="hidden" name="userId" value="<%= booking.getUserId() %>">
                    
                    <div class="form-group">
                        <label>Service</label>
                        <input type="text" value="<%= booking.getServiceName() %>" disabled>
                    </div>

                    <div class="form-group">
                        <label for="scheduledDate">Scheduled Date & Time</label>
                        <% 
                            String rawDate = booking.getScheduledDate();
                            String inputDate = "";
                            if (rawDate != null) {
                                String cleanDate = rawDate.replace(" ", "T");
                                inputDate = (cleanDate.length() >= 16) ? cleanDate.substring(0, 16) : cleanDate;
                            }
                        %>
                        <input type="datetime-local" id="scheduledDate" name="scheduledDate" value="<%= inputDate %>" required>
                    </div>

                    <div class="form-group">
                        <label for="caregiver">Assigned Caregiver</label>
                        <input type="text" id="caregiver" name="caregiver" value="<%= booking.getSpecificCaregiver() != null ? booking.getSpecificCaregiver() : "" %>">
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="Pending" <%= "Pending".equalsIgnoreCase(booking.getStatus()) ? "selected" : "" %>>Pending</option>
                            <option value="In Progress" <%= "In Progress".equalsIgnoreCase(booking.getStatus()) ? "selected" : "" %>>In Progress</option>
                            <option value="Complete" <%= "Complete".equalsIgnoreCase(booking.getStatus()) ? "selected" : "" %>>Complete</option>
                            <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(booking.getStatus()) ? "selected" : "" %>>Cancelled</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="specialRequest">Special Requests</label>
                        <textarea id="specialRequest" name="specialRequest" rows="3"><%= booking.getSpecialRequest() != null ? booking.getSpecialRequest() : "" %></textarea>
                    </div>

                    <div style="display: flex; gap: 15px; margin-top: 30px;">
                        <button type="submit" class="button button-primary" style="flex: 1;">Save Changes</button>
                        <a href="adminDashboard?tab=visits" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                    </div>
                </form>
            <% } else { %>
                <div class="status-message status-error">
                    Booking not found.
                </div>
                <a href="adminDashboard?tab=visits" class="button button-primary">Return to Dashboard</a>
            <% } %>
        </div>
    </div>

    <%@ include file="footer.html" %>
</body>
</html>
