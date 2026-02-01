<%@ page import="java.util.List" %>
<%@ page import="models.ServiceCategory" %>
<%@ page import="models.Service" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Services - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <div class="container" style="padding: 20px; max-width: 1200px; margin: 0 auto;">
        <h2 class="services-heading" style="text-align: center; margin-bottom: 30px;">
            Our Services
        </h2>
        
        <%
            List<ServiceCategory> categories = (List<ServiceCategory>) request.getAttribute("categories");
            List<Service> services = (List<Service> ) request.getAttribute("services");
            String selectedCategoryId = (String) request.getAttribute("selectedCategoryId");
        %>

        <div class="category-bar diagnostic-container" style="display: flex; flex-wrap: wrap; gap: 10px; margin: 20px auto; justify-content: center;">
            <a href="serviceCategories" class="category-btn <%= (selectedCategoryId == null || selectedCategoryId.isEmpty()) ? "active" : "" %>" style="text-decoration: none; color: #333; background-color: #f0f0f0; padding: 8px 16px; border-radius: 20px; font-weight: 500; transition: all 0.3s ease;">All</a>
            <% if (categories != null) { 
                for (ServiceCategory cat : categories) { 
                    boolean isActive = String.valueOf(cat.getId()).equals(selectedCategoryId);
            %>
                <a href="serviceCategories?category_id=<%= cat.getId() %>" class="category-btn <%= isActive ? "active" : "" %>" style="text-decoration: none; color: <%= isActive ? "white" : "#333" %>; background-color: <%= isActive ? "#007bff" : "#f0f0f0" %>; padding: 8px 16px; border-radius: 20px; font-weight: 500; transition: all 0.3s ease;">
                    <%= cat.getCategoryName() %>
                </a>
            <% } } %>
        </div>

        <div class="services-grid" style="margin-top: 30px;">
            <% if (services != null && !services.isEmpty()) { %>
                <div class="service-list" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px;">
                <% for (Service svc : services) { 
                    String imagePath = svc.getImageUrl();
                    if (imagePath == null || imagePath.trim().isEmpty()) {
                        imagePath = "images/default.png";
                    }
                %>
                    <div class="service-item" style="border: 1px solid #ddd; border-radius: 8px; overflow: hidden; display: flex; flex-direction: column;">
                        <div class="service-image" style="height: 200px; background: #eee;">
                            <img src="<%= imagePath %>" alt="<%= svc.getServiceName() %>" onerror="this.src='images/default.png'" style="width: 100%; height: 100%; object-fit: cover;">
                        </div>
                        <div class="service-content" style="padding: 15px; flex-grow: 1; display: flex; flex-direction: column;">
                            <div class="service-name" style="font-size: 1.2rem; font-weight: bold; margin-bottom: 10px;"><%= svc.getServiceName() %></div>
                            <div class="service-description" style="color: #666; font-size: 0.9rem; margin-bottom: 15px; flex-grow: 1;"><%= svc.getDescription() != null ? svc.getDescription() : "" %></div>
                            <div class="service-price" style="font-size: 1.1rem; color: #007bff; font-weight: bold; margin-bottom: 15px;">S$ <%= String.format("%.2f", svc.getPrice()) %></div>
                            <div class="book-btn">
                            <% if (session.getAttribute("user") != null) { %>
                                <a href="bookingForm.jsp?service_id=<%= svc.getId() %>&service_name=<%= java.net.URLEncoder.encode(svc.getServiceName(), "UTF-8") %>" class="button button-primary" style="display: block; text-align: center; width: 100%;">Book Now</a>
                            <% } else { %>
                                <a href="login.jsp" class="button button-secondary" style="display: block; text-align: center; width: 100%;">Login to Book</a>
                            <% } %>
                            </div>
                        </div>
                    </div>
                <% } %>
                </div>
            <% } else { %>
                <p class="no-services" style="text-align: center; color: #777;">No services found.</p>
            <% } %>
        </div>

    </div>
</main>

<%@ include file="footer.html" %>

</body>
</html>
