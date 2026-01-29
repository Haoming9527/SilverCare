<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <div class="auth-wrapper">
        <div class="auth-container">
            <div class="auth-card">
                <div class="auth-heading">
                    <h2>Register for SilverCare</h2>
                    <p>Create your account to get started with our services.</p>
                </div>

                <%
                    String errCode = request.getParameter("errCode");
                    if ("emailExists".equals(errCode)) {
                %>
                    <div class="status-message status-error">
                        Email already registered. Please use a different email or <a href="login.jsp" class="status-link">login here</a>.
                    </div>
                <%
                    } else if ("success".equals(errCode)) {
                %>
                    <div class="status-message status-success">
                        Registration successful! Please <a href="login.jsp" class="status-link">login</a> to continue.
                    </div>
                <%
                    } else if ("error".equals(errCode)) {
                %>
                    <div class="status-message status-error">
                        An unexpected error occurred. Please try again later.
                    </div>
                <%
                    }
                %>

                <form action="<%=request.getContextPath()%>/register" method="post" class="auth-form">
                    <div class="input-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" placeholder="Enter your username" required>
                    </div>

                    <div class="input-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" placeholder="Enter your email" required>
                    </div>

                    <div class="input-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Create a password" required>
                    </div>

                    <button type="submit" class="button button-primary full-width">Register</button>
                </form>

                <p class="auth-footer-text">
                    Already have an account? <a href="login.jsp">Login here</a>
                </p>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.html" %>

</body>
</html>