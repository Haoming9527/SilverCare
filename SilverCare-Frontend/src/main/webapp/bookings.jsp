<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Booking" %>
<%@ page import="models.User" %>
<%@ page import="models.Feedback" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main class="container">
    <div style="margin: 40px 0; text-align: center;">
        <h2 style="color: var(--color-primary-dark); font-size: 2.2rem; font-weight: 800; margin-bottom: 10px;">My Bookings</h2>
        <p style="color: var(--color-muted); font-size: 1.1rem;">Manage and track your service appointments</p>
    </div>

    <% 
        String successParam = request.getParameter("success");
        String errorParam = request.getParameter("error");
    %>

    <% if (successParam != null) { %>
        <div class="status-message status-success status-message-centered" style="margin-bottom: 30px;">
            <% if ("feedbackSaved".equals(successParam)) { %>
                <strong>Thank You!</strong> Your feedback has been submitted successfully.
            <% } else { %>
                <strong>Success!</strong> Your booking has been received. We'll contact you shortly.
            <% } %>
        </div>
    <% } %>

    <% if (errorParam != null) { %>
        <div class="status-message status-error status-message-centered" style="margin-bottom: 30px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 15px; border-radius: 8px;">
            <strong>Error!</strong> 
            <% if ("feedbackFailed".equals(errorParam)) { %>
                Could not save your feedback. Please try again.
            <% } else { %>
                An unexpected error occurred.
            <% } %>
        </div>
    <% } %>

    <% if (bookings == null || bookings.isEmpty()) { %>
        <div style="text-align: center; padding: 80px 20px; background: white; border-radius: 20px; box-shadow: var(--shadow-soft); margin-bottom: 60px;">
            <div style="font-size: 4rem; margin-bottom: 20px;">📅</div>
            <h3 style="color: var(--color-primary-dark); margin-bottom: 10px;">No Bookings Found</h3>
            <p style="color: var(--color-muted); margin-bottom: 25px; max-width: 400px; margin-inline: auto;">It looks like you haven't scheduled any services yet. Start your care journey today!</p>
            <a href="serviceCategories" class="button button-primary">Browse Services</a>
        </div>
    <% } else { %>
        <div class="bookings-list" style="width: 100%; max-width: 1200px; margin: 0 auto 60px;">
            <% for (Booking b : bookings) { %>
                <div class="booking-card">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 5px;">
                        <h3 style="margin: 0; border: none; padding: 0;"><%= b.getServiceName() %></h3>
                        <span class="status-badge status-<%= b.getStatus() %>"><%= b.getStatus() %></span>
                    </div>
                    
                    <div style="margin-top: 15px; display: flex; flex-direction: column; gap: 12px;">
                        <p style="border: none; padding: 0;">
                            <strong>Scheduled:</strong> 
                            <span style="color: var(--color-primary); font-weight: 600;">
                            <% 
                                String dateStr = b.getScheduledDate();
                                try {
                                    if(dateStr != null && !dateStr.isEmpty()){
                                        if(dateStr.contains(".")) dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                        java.text.SimpleDateFormat parseFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        java.util.Date date = parseFormat.parse(dateStr);
                                        out.print(dateFormat.format(date));
                                    } else {
                                        out.print("N/A");
                                    }
                                } catch(Exception e) {
                                    out.print(dateStr); 
                                }
                            %>
                            </span>
                        </p>
                        <p style="border: none; padding: 0;"><strong>Price:</strong> <span style="font-weight: 700;">S$ <%= String.format("%.2f", b.getPrice()) %></span></p>
                        <p style="border: none; padding: 0;"><strong>Caregiver:</strong> <%= (b.getSpecificCaregiver() != null && !b.getSpecificCaregiver().isEmpty()) ? b.getSpecificCaregiver() : "Any Professional" %></p>
                        <% if (b.getSpecialRequest() != null && !b.getSpecialRequest().isEmpty()) { %>
                            <p style="border: none; padding: 0; font-style: italic; color: var(--color-muted); margin-top: 5px; background: #f8f9fa; padding: 10px; border-radius: 8px;"><strong>Request:</strong> <%= b.getSpecialRequest() %></p>
                        <% } %>

                        <!-- Action Section -->
                        <div style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee;">
                        <% if ("Pending".equalsIgnoreCase(b.getStatus())) { %>
                            <a href="completeBooking?bookingId=<%= b.getId() %>" class="button button-secondary" style="width: 100%; text-align: center; text-decoration: none; display: block;">Mark as Complete</a>
                        <% } else if ("Complete".equalsIgnoreCase(b.getStatus())) { %>
                            <% 
                                java.util.Map<Integer, Feedback> feedbackMap = (java.util.Map<Integer, Feedback>) request.getAttribute("feedbackMap");
                                Feedback f = (feedbackMap != null) ? feedbackMap.get(b.getId()) : null;
                                boolean hasFeedback = f != null;
                                
                                String showFeedback = request.getParameter("showFeedback");
                                boolean isEditing = showFeedback != null && showFeedback.equals(String.valueOf(b.getId()));
                            %>
                            
                            <% if (hasFeedback && !isEditing) { %>
                                <div style="background: #eefdf5; padding: 15px; border-radius: 8px; border: 1px solid #bbf2d6;">
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                                        <strong style="color: #0f5132;">Your Feedback</strong>
                                        <span style="background: #198754; color: white; padding: 2px 8px; border-radius: 12px; font-size: 0.85rem;">★ <%= f.getRating() %></span>
                                    </div>
                                    <p style="margin: 0; color: #444; font-style: italic;">"<%= f.getComment() %>"</p>
                                    <a href="myBookings?showFeedback=<%= b.getId() %>" style="display: inline-block; margin-top: 10px; font-size: 0.9rem; color: #198754; text-decoration: underline;">Edit Feedback</a>
                                </div>
                            <% } else if (isEditing) { %>
                                <form action="saveFeedback" method="post" style="background: #f8f9fa; padding: 15px; border-radius: 10px; border: 1px solid #ddd;">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="feedbackId" value="<%= hasFeedback ? f.getId() : "" %>">
                                    
                                    <h4 style="margin-top: 0; margin-bottom: 15px;"><%= hasFeedback ? "Edit Feedback" : "Leave Feedback" %></h4>
                                    
                                    <div style="margin-bottom: 12px;">
                                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">Rating</label>
                                        <select name="rating" required style="width: 100%; padding: 8px; border-radius: 6px; border: 1px solid #ccc;">
                                            <option value="5" <%= (hasFeedback && f.getRating() == 5) ? "selected" : "" %>>5 - Excellent</option>
                                            <option value="4" <%= (hasFeedback && f.getRating() == 4) ? "selected" : "" %>>4 - Very Good</option>
                                            <option value="3" <%= (hasFeedback && f.getRating() == 3) ? "selected" : "" %>>3 - Good</option>
                                            <option value="2" <%= (hasFeedback && f.getRating() == 2) ? "selected" : "" %>>2 - Fair</option>
                                            <option value="1" <%= (hasFeedback && f.getRating() == 1) ? "selected" : "" %>>1 - Poor</option>
                                        </select>
                                    </div>
                                    <div style="margin-bottom: 15px;">
                                        <label style="display: block; margin-bottom: 5px; font-weight: 600;">Comment</label>
                                        <textarea name="comment" rows="3" style="width: 100%; padding: 8px; border-radius: 6px; border: 1px solid #ccc;" placeholder="How was your experience?"><%= hasFeedback ? f.getComment() : "" %></textarea>
                                    </div>
                                    <div style="display: flex; gap: 10px;">
                                        <button type="submit" class="button button-primary" style="flex: 1;"><%= hasFeedback ? "Update" : "Submit" %></button>
                                        <a href="myBookings" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                                    </div>
                                </form>
                            <% } else { %>
                                <a href="myBookings?showFeedback=<%= b.getId() %>" class="button button-primary" style="width: 100%; text-align: center; text-decoration: none; display: block;">Leave Feedback</a>
                            <% } %>
                        <% } %>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</main>

<%@ include file="footer.html" %>
</body>
</html>
