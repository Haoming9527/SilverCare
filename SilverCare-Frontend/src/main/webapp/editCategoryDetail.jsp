<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<%@ page import="models.ServiceCategory" %>
<%
    ServiceCategory category = (ServiceCategory) request.getAttribute("category");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Category - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h1>Edit Service Category</h1>
        <a href="adminDashboard?tab=categories" class="button button-secondary">Back to Dashboard</a>
    </div>

    <% if (category != null) { %>
    <section class="form-container">
        <form action="editCategoryManager" method="post">
            <input type="hidden" name="id" value="<%= category.getId() %>">
            <div class="input-group">
                <label for="categoryName">Category Name</label>
                <input type="text" id="categoryName" name="categoryName" value="<%= category.getCategoryName() %>" required autocomplete="off">
            </div>
            <br>
            <div style="display: flex; gap: 10px;">
                <button type="submit" class="button button-primary">Save Changes</button>
                <a href="adminDashboard?tab=categories" class="button button-secondary">Cancel</a>
            </div>
        </form>
    </section>
    <% } else { %>
        <p>Category not found.</p>
        <a href="adminDashboard?tab=categories" class="button button-primary">Return to Dashboard</a>
    <% } %>
    
</main>

<jsp:include page="footer.html" />
</body>
</html>
