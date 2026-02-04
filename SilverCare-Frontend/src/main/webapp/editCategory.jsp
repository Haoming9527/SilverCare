<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.ServiceCategory" %>
<%@ page import="java.util.List" %>
<%
    List<ServiceCategory> categories = (List<ServiceCategory>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Categories - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <h1>Manage Service Categories</h1>

    <section class="form-container" style="margin-bottom: 30px;">
        <h3>Add New Category</h3>
        <form action="manageCategory" method="post">
            <input type="hidden" name="action" value="add">
            <div class="input-group">
                <label for="categoryName">Category Name</label>
                <input type="text" id="categoryName" name="categoryName" required>
            </div>
            <button type="submit" class="button button-primary">Add Category</button>
        </form>
    </section>

    <h3>Existing Categories</h3>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Category Name</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% if (categories != null) { for (ServiceCategory cat : categories) { %>
                <tr>
                    <td><%= cat.getId() %></td>
                    <td><%= cat.getCategoryName() %></td>
                    <td>
                        <form action="manageCategory" method="post" onsubmit="return confirm('Delete this category?')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="<%= cat.getId() %>">
                            <button type="submit" class="chip-button chip-button-danger">Delete</button>
                        </form>
                    </td>
                </tr>
            <% } } %>
        </tbody>
    </table>
    
    <div style="margin-top: 20px;">
        <a href="adminDashboard" class="button button-secondary">Back to Dashboard</a>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
