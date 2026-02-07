<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.User" %>
<%
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body style="background-color: #f0f7ff; font-family: 'Inter', sans-serif;">
<%@ include file="header.jsp" %>

<main class="container" style="max-width: 800px; margin: 60px auto; padding: 0 20px;">
    <div style="background: white; border-radius: 20px; box-shadow: var(--shadow-soft); padding: 40px; border: 1px solid rgba(0,0,0,0.05);">
        <div style="text-align: center; margin-bottom: 40px;">
            <div style="width: 80px; height: 80px; background: var(--color-secondary); color: var(--color-primary); border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 2rem; margin: 0 auto 15px; font-weight: 700; border: 2px solid var(--color-primary-dark);">
                <%= user.getUsername().substring(0, 1).toUpperCase() %>
            </div>
            <h2 style="color: var(--color-primary-dark); font-size: 2rem; font-weight: 800; margin-bottom: 5px;">My Profile</h2>
            <p style="color: var(--color-muted); font-size: 1.1rem;"><%= user.getEmail() %></p>
        </div>

        <% 
            String success = request.getParameter("success");
            String error = request.getParameter("error");
        %>

        <% if ("profileUpdated".equals(success)) { %>
            <div style="background: #d1fae5; color: #065f46; padding: 15px; border-radius: 12px; margin-bottom: 30px; text-align: center; border: 1px solid #a7f3d0; font-weight: 500;">
                Your profile has been updated successfully!
            </div>
        <% } %>

        <% if (error != null) { %>
            <div style="background: #fee2e2; color: #991b1b; padding: 15px; border-radius: 12px; margin-bottom: 30px; text-align: center; border: 1px solid #fecaca; font-weight: 500;">
                <% if ("invalidPhone".equals(error)) { %>
                    Invalid phone number. Please enter exactly 8 digits.
                <% } else if ("invalidEmail".equals(error)) { %>
                    Invalid email address format.
                <% } else { %>
                    Update failed. Please try again later.
                <% } %>
            </div>
        <% } %>

        <form action="updateProfile" method="post" style="display: grid; grid-template-columns: 1fr 1fr; gap: 25px;">
            <div class="form-group" style="grid-column: span 1;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Username</label>
                <input type="text" name="username" value="<%= user.getUsername() %>" required style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem;">
            </div>

            <div class="form-group" style="grid-column: span 1;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Email Address</label>
                <input type="email" name="email" value="<%= user.getEmail() %>" required style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem;">
            </div>

            <div class="form-group" style="grid-column: span 1;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Password</label>
                <input type="password" name="password" placeholder="Leave blank to keep current" style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem;">
            </div>

            <div class="form-group" style="grid-column: span 1;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Contact Number</label>
                <div style="display: flex; align-items: center; border: 1px solid #d1d5db; border-radius: 10px; overflow: hidden; background: white;">
                    <span style="padding: 12px 15px; background: #f3f4f6; color: #374151; font-weight: 600; border-right: 1px solid #d1d5db;">+65</span>
                    <% 
                        String phoneVal = user.getPhone() != null ? user.getPhone() : "";
                        if (phoneVal.startsWith("+65")) {
                            phoneVal = phoneVal.substring(3).trim();
                        }
                    %>
                    <input type="text" name="phone" value="<%= phoneVal %>" placeholder="8000 0000" pattern="[0-9]{8}" maxlength="8" title="Please enter exactly 8 digits" required style="border: none; flex: 1; padding: 12px; font-size: 1rem; outline: none; background: transparent;">
                </div>
            </div>

            <div class="form-group" style="grid-column: span 2;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Home Address</label>
                <textarea name="address" rows="3" placeholder="Enter your full residential address" style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem; resize: none;"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea>
            </div>

            <div style="grid-column: span 2; margin: 15px 0; padding-top: 15px; border-top: 1px solid #f3f4f6;">
                <h3 style="color: var(--color-primary-dark); font-size: 1.25rem; font-weight: 700; margin-bottom: 15px;">Care & Health Information</h3>
            </div>

            <div class="form-group" style="grid-column: span 2;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Medical History / Health Info</label>
                <textarea name="healthInfo" rows="4" placeholder="Mention any medical conditions, allergies, or chronic illnesses caregivers should be aware of." style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem; resize: none;"><%= user.getHealthInfo() != null ? user.getHealthInfo() : "" %></textarea>
            </div>

            <div class="form-group" style="grid-column: span 2;">
                <label style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">Care Preferences</label>
                <textarea name="preferences" rows="4" placeholder="Any specific habits, lifestyle preferences, or daily routines you'd like us to follow." style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 10px; font-size: 1rem; resize: none;"><%= user.getPreferences() != null ? user.getPreferences() : "" %></textarea>
            </div>

            <div style="grid-column: span 2; display: flex; gap: 15px; margin-top: 20px;">
                <button type="submit" class="button button-primary" style="flex: 2; padding: 14px; font-weight: 600;">Save Profile Changes</button>
                <a href="index.jsp" class="button" style="flex: 1; padding: 14px; text-align: center; text-decoration: none; background: #f3f4f6; color: #374151; font-weight: 600;">Back to Home</a>
            </div>
        </form>
    </div>
</main>

<%@ include file="footer.html" %>
</body>
</html>
