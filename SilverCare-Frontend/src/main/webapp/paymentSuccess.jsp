<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Successful - SilverCare</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <div class="container">
        <div class="booking-success" style="padding-top: 60px; padding-bottom: 60px;">
            <div class="success-icon" style="font-size: 80px; color: #28a745; margin-bottom: 20px;">
                <span style="display: inline-block; width: 100px; height: 100px; border-radius: 50%; border: 4px solid #28a745; line-height: 100px; text-align: center;">✓</span>
            </div>
            
            <h2 style="font-size: 2.5rem; margin-bottom: 15px;">Payment Successful!</h2>
            <p style="font-size: 1.2rem; margin-bottom: 40px; color: var(--color-muted);">
                Thank you for choosing SilverCare. Your booking has been confirmed.
            </p>
            
            <div class="success-details" style="background: var(--color-secondary); padding: 30px; border-radius: var(--radius-lg); margin-bottom: 40px; text-align: left; max-width: 500px; margin-left: auto; margin-right: auto;">
                <h3 style="margin-top: 0; color: var(--color-primary-dark); font-size: 1.1rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 20px;">What's Next?</h3>
                <ul style="list-style: none; padding: 0; margin: 0; display: grid; gap: 15px;">
                    <li style="display: flex; align-items: flex-start; gap: 10px;">
                        <span style="color: var(--color-primary); font-weight: bold;">•</span>
                        <span>Our coordinator will review your request and assign a caregiver shortly.</span>
                    </li>
                    <li style="display: flex; align-items: flex-start; gap: 10px;">
                        <span style="color: var(--color-primary); font-weight: bold;">•</span>
                        <span>You will receive an email confirmation with all the booking details.</span>
                    </li>
                    <li style="display: flex; align-items: flex-start; gap: 10px;">
                        <span style="color: var(--color-primary); font-weight: bold;">•</span>
                        <span>You can track the status of your care from your dashboard.</span>
                    </li>
                </ul>
            </div>
            
            <div class="success-actions" style="display: flex; gap: 20px; justify-content: center;">
                <a href="myBookings" class="button button-primary" style="padding: 1rem 2rem;">Go to My Bookings</a>
                <a href="home.jsp" class="button button-secondary" style="padding: 1rem 2rem;">Back to Home</a>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.html" %>

</body>
</html>
