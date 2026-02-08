<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.User" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
    User editUser = (User) request.getAttribute("editUser");
    if (editUser == null) {
        response.sendRedirect("adminDashboard?tab=users&error=not_found");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Client - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .form-container {
            max-width: 650px;
            margin: 40px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div class="form-container">
        <h1 style="margin-bottom: 10px;">Edit Client Information</h1>
        <p style="color: #666; margin-bottom: 30px;">Updating profile for: <strong><%= editUser.getUsername() %></strong> (ID: <%= editUser.getId() %>)</p>
        
        <form action="editUser" method="post">
            <input type="hidden" name="id" value="<%= editUser.getId() %>">
            
            <div class="grid-2" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div class="form-group">
                    <label style="font-weight: 600; display: block; margin-bottom: 8px;">Username</label>
                    <input type="text" name="username" class="input-field" value="<%= editUser.getUsername() %>" required>
                </div>
                
                <div class="form-group">
                    <label style="font-weight: 600; display: block; margin-bottom: 8px;">Email Address</label>
                    <input type="email" name="email" class="input-field" value="<%= editUser.getEmail() %>" required>
                </div>
            </div>
            
            <div style="margin-top: 20px;" class="form-group">
                <label style="font-weight: 600; display: block; margin-bottom: 8px;">New Password (Optional)</label>
                <input type="password" name="password" class="input-field" placeholder="Leave blank to keep current password">
            </div>

            <div class="grid-2" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px;">
                <div class="form-group">
                    <label style="font-weight: 600; display: block; margin-bottom: 8px;">Role</label>
                    <select name="role" class="input-field">
                        <option value="2" <%= editUser.getRole() == 2 ? "selected" : "" %>>Customer</option>
                        <option value="1" <%= editUser.getRole() == 1 ? "selected" : "" %>>Admin</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label style="font-weight: 600; display: block; margin-bottom: 8px;">Phone Number</label>
                    <input type="text" name="phone" class="input-field" value="<%= editUser.getPhone() != null ? editUser.getPhone() : "" %>">
                </div>

                <div class="form-group">
                    <label style="font-weight: 600; display: block; margin-bottom: 8px;">Postal Code</label>
                    <input type="text" name="postalCode" class="input-field" value="<%= editUser.getPostalCode() != null ? editUser.getPostalCode() : "" %>" pattern="\d{6}" title="Please enter a 6-digit postal code" required>
                </div>
            </div>

            <div style="margin-top: 20px;" class="form-group">
                <label style="font-weight: 600; display: block; margin-bottom: 8px;">Address</label>
                <textarea name="address" class="input-field" style="height: 80px; resize: vertical;"><%= editUser.getAddress() != null ? editUser.getAddress() : "" %></textarea>
            </div>

            <div style="margin-top: 20px;" class="form-group">
                <label style="font-weight: 600; display: block; margin-bottom: 8px;">Health Information / Medical Needs</label>
                <textarea name="healthInfo" class="input-field" style="height: 80px; resize: vertical;" placeholder="Enter any relevant medical information or care needs"><%= editUser.getHealthInfo() != null ? editUser.getHealthInfo() : "" %></textarea>
            </div>

            <div style="display: flex; gap: 15px; margin-top: 40px;">
                <button type="submit" class="button button-primary" style="flex: 1;">Save Changes</button>
                <a href="adminDashboard?tab=users" class="button button-secondary" style="flex: 1; text-align: center;">Back to Dashboard</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
