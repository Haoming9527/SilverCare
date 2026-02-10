<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Feedback" %>
<%
    Feedback feedback = (Feedback) request.getAttribute("feedback");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Feedback - SilverCare Admin</title>
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
        .meta-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.9rem;
            color: #666;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container">
        <div class="edit-container">
            <div class="form-header">
                <h2>Edit Feedback</h2>
            </div>

            <% if (feedback != null) { %>
                <div class="meta-info">
                    <p><strong>User:</strong> <%= feedback.getUsername() %></p>
                    <p><strong>Service:</strong> <%= feedback.getServiceName() %></p>
                    <p><strong>Booking ID:</strong> <%= feedback.getBookingId() %></p>
                </div>

                <form action="editFeedback" method="post">
                    <input type="hidden" name="id" value="<%= feedback.getId() %>">
                    <input type="hidden" name="userId" value="<%= feedback.getUserId() %>">
                    
                    <div class="form-group">
                        <label for="rating">Rating (1-5)</label>
                        <select id="rating" name="rating" required>
                            <option value="5" <%= feedback.getRating() == 5 ? "selected" : "" %>>5 - Excellent</option>
                            <option value="4" <%= feedback.getRating() == 4 ? "selected" : "" %>>4 - Very Good</option>
                            <option value="3" <%= feedback.getRating() == 3 ? "selected" : "" %>>3 - Good</option>
                            <option value="2" <%= feedback.getRating() == 2 ? "selected" : "" %>>2 - Fair</option>
                            <option value="1" <%= feedback.getRating() == 1 ? "selected" : "" %>>1 - Poor</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="comment">Comment</label>
                        <textarea id="comment" name="comment" rows="4" required><%= feedback.getComment() %></textarea>
                    </div>

                    <div style="display: flex; gap: 15px; margin-top: 30px;">
                        <button type="submit" class="button button-primary" style="flex: 1;">Save Changes</button>
                        <a href="adminDashboard?tab=feedback" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                    </div>
                </form>
            <% } else { %>
                <div class="status-message status-error">
                    Feedback not found.
                </div>
                <a href="adminDashboard?tab=feedback" class="button button-primary">Return to Dashboard</a>
            <% } %>
        </div>
    </div>

    <%@ include file="footer.html" %>
</body>
</html>
