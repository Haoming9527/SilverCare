<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.User" %>
<%
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String serviceId = request.getParameter("service_id");
    String serviceName = request.getParameter("service_name"); 
    java.time.LocalDate today = java.time.LocalDate.now();
    String minDate = today.toString();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book a Service - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main class="container" style="max-width: 600px; margin: 40px auto; padding: 20px;">
    <div class="auth-card" style="padding: 30px;">
        <h2 style="text-align: center; margin-bottom: 20px;">Book Service: <%= serviceName %></h2>
        
        <% String error = request.getParameter("error"); %>
        <% if (error != null) { %>
            <div class="error-message" style="background: #fee2e2; color: #dc2626; padding: 10px; border-radius: 4px; margin-bottom: 20px; text-align: center; border: 1px solid #fecaca;">
                <%= error %>
            </div>
        <% } %>

        <form action="saveBooking" method="post">
            <input type="hidden" name="service_id" value="<%= serviceId %>">

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 500;">Scheduled Date</label>
                <input type="date" name="scheduled_date" min="<%= minDate %>" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 500;">Scheduled Time</label>
                <input type="time" name="scheduled_time" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 500;">Specific Caregiver (optional)</label>
                <input type="text" name="specific_caregiver" placeholder="Name of preferred caregiver" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
            </div>

            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display: block; margin-bottom: 5px; font-weight: 500;">Special Requests (optional)</label>
                <textarea name="special_request" placeholder="Any additional instructions" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; min-height: 80px;"></textarea>
            </div>

            <button type="submit" class="button button-primary" style="width: 100%; padding: 12px; font-size: 1rem;">Confirm Booking</button>
            <div style="text-align: center; margin-top: 15px;">
                <a href="serviceCategories" style="color: #666; text-decoration: none;">Cancel</a>
            </div>
        </form>
    </div>
</main>

<%@ include file="footer.html" %>
</body>
</html>
