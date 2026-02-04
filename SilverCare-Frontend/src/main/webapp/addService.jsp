<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.ServiceCategory" %>
<%@ page import="java.util.List" %>
<%
    List<ServiceCategory> categories = (List<ServiceCategory>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Service - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div class="form-container">
        <h1>Add New Service</h1>
        <p>Fill in the details below to add a new care service.</p>

        <form action="manageService" method="post">
            <input type="hidden" name="action" value="add">
            
            <div class="input-group">
                <label for="serviceName">Service Name</label>
                <input type="text" id="serviceName" name="serviceName" required>
            </div>

            <div class="input-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4" required></textarea>
            </div>

            <div class="input-group">
                <label for="price">Price ($)</label>
                <input type="number" step="0.01" id="price" name="price" required>
            </div>

            <div class="input-group">
                <label for="categoryId">Category</label>
                <select id="categoryId" name="categoryId" required>
                    <% if (categories != null) { for (ServiceCategory cat : categories) { %>
                        <option value="<%= cat.getId() %>"><%= cat.getCategoryName() %></option>
                    <% } } %>
                </select>
            </div>

            <div class="input-group">
                <label for="imageUrl">Image URL (Optional)</label>
                <input type="text" id="imageUrl" name="imageUrl">
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">Add Service</button>
                <a href="adminDashboard" class="button button-secondary">Cancel</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
