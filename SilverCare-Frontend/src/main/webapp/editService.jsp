<%@ page import="models.Service" %>
<%@ page import="models.ServiceCategory" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Service - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div class="form-container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h1>Edit Service</h1>
            <a href="adminDashboard" class="button button-secondary">Back to Dashboard</a>
        </div>
        <p>Modify the details for <strong><%= ((Service)request.getAttribute("service")).getServiceName() %></strong>.</p>

        <% Service service = (Service) request.getAttribute("service");
           List<ServiceCategory> categories = (List<ServiceCategory>) request.getAttribute("categories"); %>

        <form action="manageService" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= service.getId() %>">
            
            <div class="input-group">
                <label for="serviceName">Service Name</label>
                <input type="text" id="serviceName" name="serviceName" value="<%= service.getServiceName() %>" required>
            </div>

            <div class="input-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4" required><%= service.getDescription() %></textarea>
            </div>

            <div class="input-group">
                <label for="price">Price ($)</label>
                <input type="number" step="0.01" id="price" name="price" value="<%= service.getPrice() %>" required>
            </div>

            <div class="input-group">
                <label for="categoryId">Category</label>
                <select id="categoryId" name="categoryId" required>
                    <% if (categories != null) { for (ServiceCategory cat : categories) { %>
                        <option value="<%= cat.getId() %>" <%= cat.getId() == service.getCategoryId() ? "selected" : "" %>><%= cat.getCategoryName() %></option>
                    <% } } %>
                </select>
            </div>

            <div class="input-group">
                <label for="imageUrl">Image URL (Optional)</label>
                <input type="text" id="imageUrl" name="imageUrl" value="<%= service.getImageUrl() != null ? service.getImageUrl() : "" %>">
            </div>
			<br>
            <div class="form-actions">
                <button type="submit" class="button button-primary">Save Changes</button>
                <a href="adminDashboard" class="button button-secondary">Cancel</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
