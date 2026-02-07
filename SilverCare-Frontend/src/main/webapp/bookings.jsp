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
        String editBookingIdParam = request.getParameter("editBookingId");
        int editingId = (editBookingIdParam != null && !editBookingIdParam.isEmpty()) ? Integer.parseInt(editBookingIdParam) : -1;
    %>

    <% if (successParam != null) { %>
        <div class="status-message status-success status-message-centered" style="margin-bottom: 30px;">
            <% if ("feedbackSaved".equals(successParam)) { %>
                <strong>Thank You!</strong> Your feedback has been submitted successfully.
            <% } else if ("completed".equals(successParam)) { %>
                <strong>Excellent!</strong> Thank you for enjoying our service. We hope to see you again!
            <% } else if ("updated".equals(successParam)) { %>
                <strong>Updated!</strong> Your booking information has been successfully changed.
            <% } else if ("deleted".equals(successParam)) { %>
                <strong>Deleted!</strong> The booking history has been removed.
            <% } %>
        </div>
    <% } %>

    <% if (errorParam != null) { %>
        <div class="status-message status-error status-message-centered" style="margin-bottom: 30px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 15px; border-radius: 8px;">
            <strong>Error!</strong> 
            <% if ("feedbackFailed".equals(errorParam)) { %>
                Could not save your feedback. Please try again.
            <% } else if ("past_date".equals(errorParam)) { %>
                You cannot schedule a booking in the past. Please select a future date and time.
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
                        <span class="status-badge status-<%= b.getStatus().toLowerCase().replace(" ", "-") %>"><%= b.getStatus() %></span>
                    </div>
                    
                    <div style="margin-top: 15px; display: flex; flex-direction: column; gap: 12px;">
                        <% if (b.getCheckInTime() != null || b.getCheckOutTime() != null) { %>
                            <div style="background: #f1f4ff; padding: 10px; border-radius: 8px; font-size: 0.9rem; border: 1px dashed #4b63d1; margin-bottom: 5px;">
                                <% if (b.getCheckInTime() != null) { %>
                                    <div style="display: flex; justify-content: space-between;">
                                        <span>Arrival Time:</span>
                                        <span style="font-weight: 600;"><%= b.getCheckInTime().substring(0, 16).replace("T", " ") %></span>
                                    </div>
                                <% } %>
                                <% if (b.getCheckOutTime() != null) { %>
                                    <div style="display: flex; justify-content: space-between; margin-top: 5px;">
                                        <span>Departure Time:</span>
                                        <span style="font-weight: 600;"><%= b.getCheckOutTime().substring(0, 16).replace("T", " ") %></span>
                                    </div>
                                <% } %>
                            </div>
                        <% } %>

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
                        
                        <!-- Action Section -->
                        <div style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee;">
                        <% if ("Pending".equalsIgnoreCase(b.getStatus()) || "In Progress".equalsIgnoreCase(b.getStatus())) { %>
                            <% boolean isEditingThis = (editingId == b.getId()); %>
                            <% if (!isEditingThis) { %>
                                <div id="view-mode-<%= b.getId() %>">
                                    <p style="border: none; padding: 0;"><strong>Caregiver:</strong> <%= (b.getSpecificCaregiver() != null && !b.getSpecificCaregiver().isEmpty()) ? b.getSpecificCaregiver() : "Any Professional" %></p>
                                    <% if (b.getSpecialRequest() != null && !b.getSpecialRequest().isEmpty()) { %>
                                        <p style="border: none; padding: 0; font-style: italic; color: var(--color-muted); margin-top: 5px; background: #f8f9fa; padding: 10px; border-radius: 8px;"><strong>Request:</strong> <%= b.getSpecialRequest() %></p>
                                    <% } %>
                                    <% if ("Pending".equalsIgnoreCase(b.getStatus())) { %>
                                        <div style="display: flex; gap: 10px; margin-top: 15px;">
                                            <% if (b.getCheckOutTime() != null) { %>
                                                <a href="completeBooking?bookingId=<%= b.getId() %>" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Mark as Complete</a>
                                            <% } else { %>
                                                <a href="myBookings?editBookingId=<%= b.getId() %>" class="button button-primary" style="flex: 1; text-align: center; text-decoration: none;">Edit</a>
                                            <% } %>
                                        </div>
                                    <% } else { %>
                                        <div style="margin-top: 15px; padding: 12px; background: #e3f2fd; color: #1976d2; border-radius: 8px; text-align: center; font-weight: 600;">
                                            Caregiver is currently on-site 🏠
                                        </div>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <div id="edit-mode-<%= b.getId() %>" class="inline-edit-form">
                                    <form action="updateBooking" method="post">
                                        <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                        <div class="form-group">
                                            <label>Scheduled Date & Time</label>
                                            <% 
                                                String rawDate = b.getScheduledDate();
                                                String inputDate = "";
                                                if (rawDate != null) {
                                                    String cleanDate = rawDate.replace(" ", "T");
                                                    inputDate = (cleanDate.length() >= 16) ? cleanDate.substring(0, 16) : cleanDate;
                                                }
                                                
                                                // Calculate current date/time for min attribute
                                                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                                                String minDateTime = now.toString().substring(0, 16);
                                            %>
                                            <input type="datetime-local" name="scheduledDate" value="<%= inputDate %>" min="<%= minDateTime %>" required>
                                        </div>
                                        <div class="form-group">
                                            <label>Preferred Caregiver</label>
                                            <input type="text" name="caregiver" value="<%= (b.getSpecificCaregiver() != null) ? b.getSpecificCaregiver() : "" %>" placeholder="e.g. Nurse Jamie">
                                        </div>
                                        <div class="form-group">
                                            <label>Special Requests</label>
                                            <textarea name="specialRequest" rows="2" placeholder="Any special needs?"><%= (b.getSpecialRequest() != null) ? b.getSpecialRequest() : "" %></textarea>
                                        </div>
                                        <div class="button-group">
                                            <button type="submit" class="button button-primary" style="flex: 1;">Save</button>
                                            <a href="myBookings" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                                        </div>
                                    </form>
                                </div>
                            <% } %>
                        <% } else if ("Complete".equalsIgnoreCase(b.getStatus())) { %>
                            <p style="border: none; padding: 0; margin-bottom: 12px;"><strong>Caregiver:</strong> <%= (b.getSpecificCaregiver() != null && !b.getSpecificCaregiver().isEmpty()) ? b.getSpecificCaregiver() : "Any Professional" %></p>
                            <div style="display: flex; flex-direction: column; gap: 10px;">
                                <form action="deleteBooking" method="post" onsubmit="return confirm('Are you sure you want to delete this booking history?');">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <button type="submit" class="button button-danger" style="width: 100%;">Delete History</button>
                                </form>
                            <% 
                                java.util.Map<Integer, Feedback> feedbackMap = (java.util.Map<Integer, Feedback>) request.getAttribute("feedbackMap");
                                Feedback f = (feedbackMap != null) ? feedbackMap.get(b.getId()) : null;
                                boolean hasFeedback = f != null;
                                
                                String showFeedback = request.getParameter("showFeedback");
                                boolean isEditingFeedback = showFeedback != null && showFeedback.equals(String.valueOf(b.getId()));
                            %>
                            
                            <% if (hasFeedback && !isEditingFeedback) { %>
                                <div style="background: #eefdf5; padding: 15px; border-radius: 8px; border: 1px solid #bbf2d6;">
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                                        <strong style="color: #0f5132;">Your Feedback</strong>
                                        <span style="background: #198754; color: white; padding: 2px 8px; border-radius: 12px; font-size: 0.85rem;">★ <%= f.getRating() %></span>
                                    </div>
                                    <p style="margin: 0; color: #444; font-style: italic;">"<%= f.getComment() %>"</p>
                                    <a href="myBookings?showFeedback=<%= b.getId() %>" style="display: inline-block; margin-top: 10px; font-size: 0.9rem; color: #198754; text-decoration: underline;">Edit Feedback</a>
                                </div>
                            <% } else if (isEditingFeedback) { %>
                                <form action="saveFeedback" method="post" style="background: #f8f9fa; padding: 15px; border-radius: 10px; border: 1px solid #ddd;">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="feedbackId" value="<%= hasFeedback ? f.getId() : "" %>">
                                    
                                    <h4 style="margin-top: 0; margin-bottom: 15px;"><%= hasFeedback ? "Edit Feedback" : "Leave Feedback" %></h4>
                                    
                                    <select name="rating" required style="width: 100%; padding: 8px; margin-bottom: 10px; border-radius: 6px; border: 1px solid #ccc;">
                                        <option value="5" <%= (hasFeedback && f.getRating() == 5) ? "selected" : "" %>>5 - Excellent</option>
                                        <option value="4" <%= (hasFeedback && f.getRating() == 4) ? "selected" : "" %>>4 - Very Good</option>
                                        <option value="3" <%= (hasFeedback && f.getRating() == 3) ? "selected" : "" %>>3 - Good</option>
                                        <option value="2" <%= (hasFeedback && f.getRating() == 2) ? "selected" : "" %>>2 - Fair</option>
                                        <option value="1" <%= (hasFeedback && f.getRating() == 1) ? "selected" : "" %>>1 - Poor</option>
                                    </select>
                                    <textarea name="comment" rows="3" style="width: 100%; padding: 8px; margin-bottom: 10px; border-radius: 6px; border: 1px solid #ccc;" placeholder="How was your experience?"><%= hasFeedback ? f.getComment() : "" %></textarea>
                                    <div style="display: flex; gap: 10px;">
                                        <button type="submit" class="button button-primary" style="flex: 1;"><%= hasFeedback ? "Update" : "Submit" %></button>
                                        <a href="myBookings" class="button button-secondary" style="flex: 1; text-align: center; text-decoration: none;">Cancel</a>
                                    </div>
                                </form>
                            <% } else { %>
                                <a href="myBookings?showFeedback=<%= b.getId() %>" class="button button-primary" style="width: 100%; text-align: center; text-decoration: none; display: block;">Leave Feedback</a>
                            <% } %>
                            </div>
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
