<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Contact Us - SilverCare</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <section class="contact-hero">
        <div class="container">
            <div class="contact-hero-content">
                <h1>Contact Us</h1>
                <p class="contact-subtitle">
                    We're here to help. Get in touch with our team for any questions or inquiries.
                </p>
            </div>
        </div>
    </section>

    <section class="contact-content">
        <div class="container">
            <div class="contact-wrapper">
                <div class="contact-info-section">
                    <h2>Get in Touch</h2>
                    <p>
                        Whether you have questions about our services, need assistance with booking, 
                        or want to learn more about how SilverCare can help you or your loved one, 
                        we're here to assist you.
                    </p>

                    <div class="contact-methods">
                        <div class="contact-method-card">
                            <div class="contact-icon">📞</div>
                            <h3>Phone</h3>
                            <p>Call us during business hours</p>
                            <a href="tel:+6512345678" class="contact-link">+65 6123 4567</a>
                            <span class="contact-hours">Mon - Fri: 8:00 AM - 6:00 PM</span>
                        </div>

                        <div class="contact-method-card">
                            <div class="contact-icon">✉️</div>
                            <h3>Email</h3>
                            <p>Send us an email anytime</p>
                            <a href="mailto:info@silvercare.com" class="contact-link">info@silvercare.com</a>
                            <span class="contact-hours">We respond within 24 hours</span>
                        </div>

                        <div class="contact-method-card">
                            <div class="contact-icon">📍</div>
                            <h3>Address</h3>
                            <p>Visit our office</p>
                            <address class="contact-link">
                                123 Care Avenue<br>
                                #05-10 SilverCare Building<br>
                                Singapore 123456
                            </address>
                            <span class="contact-hours">Mon - Fri: 9:00 AM - 5:00 PM</span>
                        </div>
                    </div>

                    <div class="emergency-contact">
                        <h3>Emergency Contact</h3>
                        <p>For urgent care needs outside business hours:</p>
                        <a href="tel:+6598765432" class="emergency-link">+65 9876 5432</a>
                        <span class="emergency-note">Available 24/7 for emergencies</span>
                    </div>
                </div>

                <%
                    String formStatus = request.getParameter("status");
                    String statusClass = null;
                    String statusText = null;
                    if ("success".equals(formStatus)) {
                        statusClass = "status-message status-success status-message-centered";
                        statusText = "Thank you for reaching out! Our team will contact you soon.";
                    } else if ("error".equals(formStatus)) {
                        statusClass = "status-message status-error status-message-centered";
                        statusText = "Something went wrong while sending your message. Please try again.";
                    } else if ("missing".equals(formStatus)) {
                        statusClass = "status-message status-warning status-message-centered";
                        statusText = "Please complete all required fields before submitting.";
                    }
                %>

                <div class="contact-form-section">
                    <h2>Send us a Message</h2>
                    <p>Fill out the form below and we'll get back to you as soon as possible.</p>
                    
                    <%
                        if (statusText != null) {
                    %>
                    <div class="<%= statusClass %>" style="margin-bottom: 20px;"><%= statusText %></div>
                    <%
                        }
                    %>
                    
                    <form class="contact-form" method="POST" action="contact">
                        <div class="input-group">
                            <label for="name">Full Name *</label>
                            <input type="text" id="name" name="name" placeholder="Enter your full name" required>
                        </div>

                        <div class="input-group">
                            <label for="email">Email Address *</label>
                            <input type="email" id="email" name="email" placeholder="your.email@example.com" required>
                        </div>

                        <div class="input-group">
                            <label for="phone">Phone Number</label>
                            <input type="tel" id="phone" name="phone" placeholder="+65 1234 5678">
                        </div>

                        <div class="input-group">
                            <label for="subject">Subject *</label>
                            <select id="subject" name="subject" required>
                                <option value="">Select a subject</option>
                                <option value="general">General Inquiry</option>
                                <option value="services">Service Information</option>
                                <option value="booking">Booking Request</option>
                                <option value="support">Customer Support</option>
                                <option value="feedback">Feedback</option>
                                <option value="other">Other</option>
                            </select>
                        </div>

                        <div class="input-group">
                            <label for="message">Message *</label>
                            <textarea id="message" name="message" rows="6" placeholder="Tell us how we can help you..." required></textarea>
                        </div>

                        <button type="submit" class="button button-primary full-width">Send Message</button>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <section class="office-hours">
        <div class="container">
            <h2>Office Hours</h2>
            <div class="hours-grid">
                <div class="hours-card">
                    <h3>Monday - Friday</h3>
                    <p class="hours-time">8:00 AM - 6:00 PM</p>
                </div>
                <div class="hours-card">
                    <h3>Saturday</h3>
                    <p class="hours-time">9:00 AM - 2:00 PM</p>
                </div>
                <div class="hours-card">
                    <h3>Sunday</h3>
                    <p class="hours-time">Closed</p>
                </div>
                <div class="hours-card">
                    <h3>Public Holidays</h3>
                    <p class="hours-time">Closed</p>
                </div>
            </div>
            <p class="hours-note">
                <strong>Note:</strong> Emergency services are available 24/7. Please call our emergency hotline for urgent care needs.
            </p>
        </div>
    </section>
</main>

<%@ include file="footer.html" %>

</body>
</html>
