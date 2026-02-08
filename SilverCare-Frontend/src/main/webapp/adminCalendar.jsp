<%
    String role = (String) session.getAttribute("role");
    if (!"Admin".equals(role)) {
        response.sendRedirect("login.jsp?errCode=unauthorized");
        return;
    }
%>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="models.Booking" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Calendar (Static) - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .calendar-table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .calendar-table th { background: #4b63d1; color: white; padding: 15px; width: 14.28%; }
        .calendar-table td { border: 1px solid #eee; height: 120px; vertical-align: top; padding: 10px; width: 14.28%; }
        .date-num { font-weight: bold; margin-bottom: 5px; color: #666; }
        .booking-entry { font-size: 0.75rem; padding: 4px 8px; border-radius: 4px; margin-bottom: 3px; color: white; }
        .status-confirmed { background: #2ecc71; }
        .status-progress { background: #f1c40f; color: #333; }
        .status-pending { background: #4b63d1; }
        .status-cancelled { background: #e74c3c; }
        .today { background: #f0f4ff; }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <% 
            LocalDate today = LocalDate.now();
            int monthValue = today.getMonthValue();
            int yearValue = today.getYear();
            
            String mParam = request.getParameter("month");
            String yParam = request.getParameter("year");
            if (mParam != null) monthValue = Integer.parseInt(mParam);
            if (yParam != null) yearValue = Integer.parseInt(yParam);
            
            LocalDate displayDate = LocalDate.of(yearValue, monthValue, 1);
            String monthName = displayDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            
            LocalDate prevMonth = displayDate.minusMonths(1);
            LocalDate nextMonth = displayDate.plusMonths(1);
        %>
        <div>
            <h1><%= monthName %> <%= yearValue %></h1>
            <p>Schedule view of all care sessions</p>
        </div>
        <div style="display: flex; gap: 10px; align-items: center;">
            <a href="adminCalendar?month=<%= prevMonth.getMonthValue() %>&year=<%= prevMonth.getYear() %>" class="button button-secondary">&laquo; Prev</a>
            <a href="adminCalendar" class="button button-secondary">Today</a>
            <a href="adminCalendar?month=<%= nextMonth.getMonthValue() %>&year=<%= nextMonth.getYear() %>" class="button button-secondary">Next &raquo;</a>
            <a href="adminDashboard" class="button button-secondary" style="margin-left: 10px;">Back to Dashboard</a>
        </div>
    </div>

    <table class="calendar-table">
        <thead>
            <tr>
                <th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th>
            </tr>
        </thead>
        <tbody>
            <% 
                int startDayOfWeek = displayDate.getDayOfWeek().getValue() % 7; // Sunday = 0
                int daysInMonth = displayDate.lengthOfMonth();
                int currentDay = 1;
                
                List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                Map<Integer, List<Booking>> bookingsByDay = new HashMap<>();
                if (bookings != null) {
                    for (Booking b : bookings) {
                        try {
                            // Extract day from YYYY-MM-DD HH:mm:ss
                            String dateStr = b.getScheduledDate().split(" ")[0];
                            LocalDate bDate = LocalDate.parse(dateStr);
                            if (bDate.getMonthValue() == monthValue && bDate.getYear() == yearValue) {
                                int day = bDate.getDayOfMonth();
                                bookingsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(b);
                            }
                        } catch (Exception e) {}
                    }
                }

                for (int row = 0; row < 6; row++) {
                    if (currentDay > daysInMonth) break;
            %>
                <tr>
                    <% for (int col = 0; col < 7; col++) {
                        int dayIdx = row * 7 + col;
                        if (dayIdx < startDayOfWeek || currentDay > daysInMonth) {
                    %>
                        <td></td>
                    <% } else { 
                        boolean isToday = LocalDate.now().equals(LocalDate.of(yearValue, monthValue, currentDay));
                    %>
                        <td class="<%= isToday ? "today" : "" %>">
                            <div class="date-num"><%= currentDay %></div>
                            <% 
                                List<Booking> dayBookings = bookingsByDay.get(currentDay);
                                if (dayBookings != null) {
                                    for (Booking b : dayBookings) {
                                        String statusClass = "status-pending";
                                        if ("Confirmed".equalsIgnoreCase(b.getStatus())) statusClass = "status-confirmed";
                                        else if ("In Progress".equalsIgnoreCase(b.getStatus())) statusClass = "status-progress";
                                        else if ("Cancelled".equalsIgnoreCase(b.getStatus())) statusClass = "status-cancelled";
                            %>
                                <div class="booking-entry <%= statusClass %>" title="<%= b.getServiceName() %> - <%= b.getStatus() %>">
                                    <%= b.getServiceName().length() > 15 ? b.getServiceName().substring(0, 15) + "..." : b.getServiceName() %>
                                </div>
                            <% 
                                    }
                                }
                            %>
                        </td>
                    <% 
                        currentDay++;
                       }
                    } %>
                </tr>
            <% } %>
        </tbody>
    </table>
</main>

<jsp:include page="footer.html" />
</body>
</html>
