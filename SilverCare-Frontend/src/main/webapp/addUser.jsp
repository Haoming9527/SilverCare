<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Client - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .form-container {
            max-width: 600px;
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
        <h1 style="margin-bottom: 20px;">Add New Client</h1>
        <p style="color: #666; margin-bottom: 30px;">Complete the form below to register a new client or caregiver account.</p>
        
        <form action="addUser" method="post" class="form-group">
            <div style="margin-bottom: 20px;">
                <label for="username" style="display: block; margin-bottom: 8px; font-weight: 600;">Username</label>
                <input type="text" name="username" id="username" class="input-field" placeholder="Full Name" required>
            </div>
            
            <div style="margin-bottom: 20px;">
                <label for="email" style="display: block; margin-bottom: 8px; font-weight: 600;">Email Address</label>
                <input type="email" name="email" id="email" class="input-field" placeholder="email@example.com" required>
            </div>
            
            <div style="margin-bottom: 20px;">
                <label for="password" style="display: block; margin-bottom: 8px; font-weight: 600;">Password</label>
                <input type="password" name="password" id="password" class="input-field" placeholder="Min 6 characters" required>
            </div>
            

            <div style="margin-bottom: 20px;">
                <label for="role" style="display: block; margin-bottom: 8px; font-weight: 600;">User Role</label>
                <select name="role" id="role" class="input-field">
                    <option value="2">Customer</option>
                    <option value="1">Admin</option>
                </select>
            </div>

            <div style="display: flex; gap: 15px; margin-top: 30px;">
                <button type="submit" class="button button-primary" style="flex: 1;">Create Account</button>
                <a href="adminDashboard?tab=users" class="button button-secondary" style="flex: 1; text-align: center;">Cancel</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
