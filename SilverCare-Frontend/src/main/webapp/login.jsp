<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div class="auth-wrapper">
        <div class="auth-container">
            <div class="auth-card">
                <div class="auth-heading">
                    <h2>Login to SilverCare</h2>
                </div>

                <%
                    String errCode = request.getParameter("errCode");
                    if ("invalid".equals(errCode != null ? errCode.trim() : "")) {
                %>
                    <div class="status-message status-error">
                        You have entered an invalid email or password.
                    </div>
                <%
                    }
                %>

                <form action="<%=request.getContextPath()%>/login" method="post" class="auth-form">
                    <div class="input-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" placeholder="Enter your email" required>
                    </div>

                    <div class="input-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    </div>

                    <button type="submit" class="button button-primary full-width">Login</button>
                </form>

                <p>
                    Don't have an account? <a href="register.jsp">Register here</a>
                </p>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.html" %>
</body>
</html>
