<%@ page session="true" %>
<header class="site-header">
    <div class="header-inner">
        <h1 class="logo">
            <a href="index.jsp">SilverCare</a>
        </h1>
        <nav class="main-nav">
            <ul>
                <%
                    Object roleObj = (session != null) ? session.getAttribute("role") : null;
                    String userRole = (roleObj != null) ? roleObj.toString() : null;
                    boolean isAdmin = "Admin".equals(userRole);
                    boolean isCustomer = "Customer".equals(userRole);

                    if (!isAdmin) {
                %>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="about.jsp">About Us</a></li>
                <li><a href="serviceCategories">Services</a></li>
                <li><a href="contact">Contact Us</a></li>
                <%
                    }

                    if (userRole == null) {
                %>
                <li><a href="login" class="button button-primary">Login</a></li>
                <%
                    } else if (isAdmin) {
                %>
                <li><a href="adminDashboard">Admin Dashboard</a></li>
                <li><a href="logs">Activity Logs</a></li>
                <li><a href="logout" class="button button-secondary">Logout</a></li>
                <%
                    } else if (isCustomer) {
                %>
                <li><a href="myBookings">My Bookings</a></li>
                <li><a href="profile">My Profile</a></li>
                <li><a href="logout" class="button button-secondary">Logout</a></li>
                <%
                    } else {
                %>
                <li><a href="logout" class="button button-secondary">Logout</a></li>
                <%
                    }
                %>
            </ul>
        </nav>
    </div>
</header>
