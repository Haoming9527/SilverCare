<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<%@ page import="java.util.List, java.util.Map, models.RevenueTrend, models.ClientAnalytics, models.Service, models.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Analytics - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .analytics-grid { 
            display: grid; 
            grid-template-columns: 1fr 1fr; 
            gap: 20px; 
            margin-top: 25px;
            width: 100%;
        }
        .analytics-card { 
            background: white; 
            padding: 20px; 
            border-radius: 12px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.06); 
            border: 1px solid #eee;
            min-width: 0; 
            display: flex;
            flex-direction: column;
        }
        .full-width-card { grid-column: 1 / 3; }
        
        .chart-scroll-area {
            width: 100%;
            overflow-x: auto;
            margin-top: 15px;
            border-radius: 6px;
            background: #fafafa;
            border: 1px solid #f0f0f0;
        }
        .bar-chart-container { 
            display: flex; 
            align-items: flex-end; 
            justify-content: space-around; 
            height: 280px; 
            padding: 20px 10px;
            min-width: 100%; /* Shrink to fit if few bars */
            width: max-content; /* Expand if many bars */
            box-sizing: border-box;
            gap: 15px;
        }
        .bar-column { 
            text-align: center; 
            flex: 0 0 50px;
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            height: 100%;
        }
        .bar { 
            background: #4b63d1; 
            min-height: 2px; 
            border-radius: 4px 4px 0 0; 
            width: 30px; 
            margin: 0 auto;
        }
        .value-label { font-size: 0.7rem; margin-bottom: 5px; color: #4b63d1; font-weight: bold; }
        .month-label { font-size: 0.7rem; margin-top: 8px; color: #666; font-weight: bold; }

        .table-responsive {
            width: 100%;
            overflow-x: auto;
            margin-top: 10px;
            border-radius: 8px;
            border: 1px solid #eee;
        }
        .data-table-compact {
            width: 100%;
            border-collapse: collapse;
            font-size: 0.85rem;
            min-width: 450px; /* Reduced from global 720px to fit cards better */
        }
        .data-table-compact th, .data-table-compact td {
            padding: 10px 12px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }
        .data-table-compact th {
            background: #f8f9fa;
            color: #555;
            font-weight: 600;
        }

        @media (max-width: 850px) {
            .analytics-grid { grid-template-columns: 1fr; }
            .full-width-card { grid-column: 1; }
        }

        .badge {
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: 600;
            color: white;
            display: inline-block;
        }
        .badge-high { background-color: #e67e22; }
        .badge-medium { background-color: #3498db; }
        .badge-low { background-color: #95a5a6; }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px;">
        <div>
            <h1>Admin Analytics</h1>
            <p style="color: #666;">Insights into revenue, clients, and service performance.</p>
        </div>
        <a href="adminDashboard" class="button button-secondary">Back to Dashboard</a>
    </div>

    <div class="analytics-grid">
        <%-- Revenue Trends --%>
        <div class="analytics-card full-width-card">
            <h3 style="margin-bottom: 5px;">Monthly Revenue Trend</h3>
            <p style="font-size: 0.9rem; color: #888; margin-bottom: 10px;">Cumulative revenue from confirmed care bookings.</p>
            
            <div class="chart-scroll-area">
                <div class="bar-chart-container">
                <% 
                    List<models.RevenueTrend> trends = (List<models.RevenueTrend>) request.getAttribute("revenueTrend");
                    if (trends == null || trends.isEmpty()) {
                %>
                    <div style="width: 100%; text-align: center; color: #999; padding: 100px 0;">
                        <p>No revenue data available for the selected period.</p>
                    </div>
                <% 
                    } else {
                        double peak = 0;
                        for (models.RevenueTrend t : trends) {
                            if (t.getRevenue() > peak) peak = t.getRevenue();
                        }
                        
                        // Use a target height percentage for visual balance
                        for (models.RevenueTrend t : trends) {
                            double revenue = t.getRevenue();
                            int h = (peak > 0) ? (int)((revenue / peak) * 100) : 0;
                            // Ensure a minimum visual height if revenue > 0
                            if (revenue > 0 && h < 2) h = 2;
                %>
                    <div class="bar-column">
                        <div class="value-label">S$<%= String.format("%.0f", revenue) %></div>
                        <div class="bar" <%= "style=\"height: " + h + "%;\"" %> title="S$<%= revenue %> in <%= t.getMonth() %>"></div>
                        <div class="month-label"><%= (t.getMonth() != null ? t.getMonth() : "Unknown") %></div>
                    </div>
                <% 
                        }
                    }
                %>
                </div>
            </div>
        </div>

        <div class="analytics-card">
            <h3>Top 3 Clients</h3>
            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Bookings</th>
                            <th>Spent</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<ClientAnalytics> topClients = (List<ClientAnalytics>) request.getAttribute("topClients");
                            if (topClients != null) {
                                for (ClientAnalytics client : topClients) {
                        %>
                            <tr>
                                <td><strong><%= client.getUsername() %></strong></td>
                                <td><%= client.getTotalBookings() %></td>
                                <td>S$<%= String.format("%.0f", client.getTotalSpent()) %></td>
                            </tr>
                        <% 
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- Clients by Service --%>
        <div class="analytics-card">
            <h3>Find Clients by Service</h3>
            <form action="adminAnalytics" method="get" style="margin-bottom: 15px; display: flex; gap: 10px;">
                <select name="serviceId" class="input-field" style="flex: 1; padding: 8px; border-radius: 4px; border: 1px solid #ddd;">
                    <option value="">Select a Service...</option>
                    <% 
                        List<Service> services = (List<Service>) request.getAttribute("servicesList");
                        String selectedId = (String) request.getAttribute("selectedServiceId");
                        if (services != null) {
                            for (Service s : services) {
                    %>
                        <option value="<%= s.getId() %>" <%= String.valueOf(s.getId()).equals(selectedId) ? "selected" : "" %>><%= s.getServiceName() %></option>
                    <% 
                            }
                        }
                    %>
                </select>
                <button type="submit" class="button button-primary">Filter</button>
            </form>

            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Email</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<User> filteredUsers = (List<User>) request.getAttribute("filteredUsers");
                            if (selectedId == null || selectedId.isEmpty()) {
                        %>
                            <tr><td colspan="2" style="text-align: center;">Select a service to see clients.</td></tr>
                        <% 
                            } else if (filteredUsers == null || filteredUsers.isEmpty()) {
                        %>
                            <tr><td colspan="2" style="text-align: center;">No clients have booked this service yet.</td></tr>
                        <% 
                            } else {
                                for (User u : filteredUsers) {
                        %>
                            <tr>
                                <td><strong><%= u.getUsername() %></strong></td>
                                <td><%= u.getEmail() %></td>
                            </tr>
                        <% 
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- Service Ratings --%>
        <div class="analytics-card">
            <h3>Service Ratings</h3>
            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Service</th>
                            <th>Rating</th>
                            <th>Reviews</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Map<String, Object>> serviceRatings = (List<Map<String, Object>>) request.getAttribute("serviceRatings");
                            if (serviceRatings != null && !serviceRatings.isEmpty()) {
                                for (Map<String, Object> entry : serviceRatings) {
                        %>
                            <tr>
                                <td><strong><%= entry.get("serviceName") %></strong></td>
                                <td>
                                    <span style="color: #f1c40f;">★</span> 
                                    <%= String.format("%.1f", ((Number)entry.get("avgRating")).doubleValue()) %>
                                </td>
                                <td><%= entry.get("feedbackCount") %></td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr><td colspan="3" style="text-align: center;">No feedback data yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- Caregiver Ratings --%>
        <div class="analytics-card">
            <h3>Caregiver Performance</h3>
            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Caregiver</th>
                            <th>Rating</th>
                            <th>Reviews</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Map<String, Object>> caregiverRatings = (List<Map<String, Object>>) request.getAttribute("caregiverRatings");
                            if (caregiverRatings != null && !caregiverRatings.isEmpty()) {
                                for (Map<String, Object> entry : caregiverRatings) {
                        %>
                            <tr>
                                <td><strong><%= entry.get("caregiverName") %></strong></td>
                                <td>
                                    <span style="color: #f1c40f;">★</span> 
                                    <%= String.format("%.1f", ((Number)entry.get("avgRating")).doubleValue()) %>
                                </td>
                                <td><%= entry.get("feedbackCount") %></td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr><td colspan="3" style="text-align: center;">No caregiver reviews yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- High Demand Services --%>
        <div class="analytics-card full-width-card">
            <h3>Service Demand</h3>
            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Service Name</th>
                            <th>Total Bookings</th>
                            <th>Demand Level</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Map<String, Object>> serviceDemand = (List<Map<String, Object>>) request.getAttribute("serviceDemand");
                            if (serviceDemand != null && !serviceDemand.isEmpty()) {
                                int maxBookings = 0;
                                for(Map<String, Object> d : serviceDemand) {
                                    int countValue = ((Number)d.get("bookingCount")).intValue();
                                    if(countValue > maxBookings) maxBookings = countValue;
                                }

                                for (Map<String, Object> entry : serviceDemand) {
                                    int countValue = ((Number)entry.get("bookingCount")).intValue();
                                    String demandLabel = "Low";
                                    String badgeClass = "badge-low";
                                    
                                    if (countValue >= maxBookings * 0.7 && countValue > 0) {
                                        demandLabel = "High";
                                        badgeClass = "badge-high";
                                    } else if (countValue >= maxBookings * 0.3) {
                                        demandLabel = "Medium";
                                        badgeClass = "badge-medium";
                                    }
                        %>
                            <tr>
                                <td><strong><%= entry.get("serviceName") %></strong></td>
                                <td><%= countValue %></td>
                                <td>
                                    <span class="badge <%= badgeClass %>">
                                        <%= demandLabel %>
                                    </span>
                                </td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr><td colspan="3" style="text-align: center;">No booking data yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- Clients by Residential Area --%>
        <div class="analytics-card full-width-card">
            <h3>Clients by Residential Area</h3>
            <p style="font-size: 0.9rem; color: #888; margin-bottom: 10px;">Grouping based on the first 2 digits of the 6-digit postal code (Singapore Sectors).</p>
            <div class="table-responsive">
                <table class="data-table-compact">
                    <thead>
                        <tr>
                            <th>Postal Code</th>
                            <th>Number of Clients</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Map<String, Object>> areaDist = (List<Map<String, Object>>) request.getAttribute("areaDistribution");
                            if (areaDist != null && !areaDist.isEmpty()) {
                                for (Map<String, Object> entry : areaDist) {
                                    String areaCode = (String) entry.get("areaCode");
                                    String description = "Residential Area " + areaCode;
                                    
                                    // Basic mapping for common SG sectors
                                    if ("01".equals(areaCode) || "02".equals(areaCode) || "03".equals(areaCode)) description = "Raffles Place, Cecil, Marina";
                                    else if ("04".equals(areaCode) || "05".equals(areaCode)) description = "Harbourfront, Telok Blangah";
                                    else if ("12".equals(areaCode) || "13".equals(areaCode)) description = "Balestier, Toa Payoh, Serangoon";
                                    else if ("14".equals(areaCode) || "15".equals(areaCode) || "16".equals(areaCode)) description = "Geylang, Eunos, Katong, Marine Parade";
                                    else if ("18".equals(areaCode) || "19".equals(areaCode)) description = "Tampines, Pasir Ris, Serangoon Garden, Hougang, Punggol";
                                    else if ("22".equals(areaCode) || "23".equals(areaCode)) description = "Jurong, Bukit Batok, Bukit Panjang, Choa Chu Kang";
                                    else if ("25".equals(areaCode) || "26".equals(areaCode) || "27".equals(areaCode)) description = "Kranji, Woodgrove, Yishun, Sembawang";
                        %>
                            <tr>
                                <td><strong>Sector <%= areaCode %></strong></td>
                                <td><%= entry.get("clientCount") %></td>
                                <td style="color: #666;"><%= description %></td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr><td colspan="3" style="text-align: center;">No residential distribution data available yet.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<jsp:include page="footer.html" />
</body>
</html>
