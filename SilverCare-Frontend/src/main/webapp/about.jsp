<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SilverCare - About Us</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <section class="about-hero">
        <div class="container">
            <div class="about-hero-content">
                <h1>About SilverCare</h1>
                <p class="about-subtitle">
                    Dedicated to delivering comfort, safety, and independence at home
                </p>
            </div>
        </div>
    </section>

    <section class="about-content">
        <div class="container">
            <div class="about-section">
                <h2>Our Mission</h2>
                <p>
                    At SilverCare, we believe that every senior deserves to age with dignity, comfort, and independence. 
                    Our mission is to provide compassionate, professional care services that enable older adults to thrive 
                    in their own homes while maintaining their quality of life and personal autonomy.
                </p>
            </div>

            <div class="about-section">
                <h2>What We Do</h2>
                <p>
                    SilverCare is a comprehensive senior care service provider offering a wide range of professional 
                    in-home support, assisted living services, and personalized therapy programs. We understand that 
                    each individual has unique needs, and we tailor our services to meet those specific requirements.
                </p>
                
                <div class="services-overview">
                    <div class="service-type-card">
                        <h3>In-Home Support</h3>
                        <p>
                            Our certified caregivers provide daily living assistance, personal care, medication reminders, 
                            and companionship services. Whether you need help with meal preparation, housekeeping, or 
                            simply someone to talk to, our team is here to support you.
                        </p>
                    </div>
                    <div class="service-type-card">
                        <h3>Assisted Living Services</h3>
                        <p>
                            We offer comprehensive assisted living support that helps seniors maintain their independence 
                            while receiving the care they need. Our services include mobility assistance, health monitoring, 
                            and coordination with healthcare providers.
                        </p>
                    </div>
                    <div class="service-type-card">
                        <h3>Personalized Therapy</h3>
                        <p>
                            Our licensed therapists provide physical, occupational, and speech therapy services designed 
                            to improve mobility, strength, and overall wellness. Each therapy program is customized to 
                            address individual health goals and recovery needs.
                        </p>
                    </div>
                </div>
            </div>

            <div class="about-section">
                <h2>Why Choose SilverCare</h2>
                <div class="values-grid">
                    <article class="value-card">
                        <h3>Trusted Professionals</h3>
                        <p>
                            All our caregivers, nurses, and therapists are certified, licensed, and undergo thorough 
                            background checks. We carefully match each professional to your specific needs and preferences, 
                            ensuring you receive the highest quality care.
                        </p>
                    </article>
                    <article class="value-card">
                        <h3>Flexible Scheduling</h3>
                        <p>
                            We understand that care needs can vary. That's why we offer flexible scheduling options including 
                            one-time visits, recurring care appointments, and on-call assistance. Our services adapt to your 
                            routine, not the other way around.
                        </p>
                    </article>
                    <article class="value-card">
                        <h3>Holistic Wellness</h3>
                        <p>
                            We take a comprehensive approach to senior care, addressing not just physical needs but also 
                            emotional and social well-being. From daily living support to therapy and companionship, we care 
                            for the whole person.
                        </p>
                    </article>
                    <article class="value-card">
                        <h3>Compassionate Care</h3>
                        <p>
                            Every member of our team is committed to treating clients with respect, empathy, and dignity. 
                            We build meaningful relationships with our clients and their families, creating a supportive 
                            care environment.
                        </p>
                    </article>
                </div>
            </div>

            <div class="about-section">
                <h2>Our Commitment</h2>
                <p>
                    SilverCare is committed to excellence in everything we do. We continuously train our staff, update our 
                    service offerings, and improve our processes to ensure we provide the best possible care. Your safety, 
                    comfort, and satisfaction are our top priorities.
                </p>
                <p>
                    We work closely with families to create personalized care plans that respect the wishes and preferences 
                    of our clients. Our transparent communication and regular updates keep families informed and involved in 
                    their loved one's care journey.
                </p>
            </div>

            <div class="about-cta">
                <h2>Ready to Get Started?</h2>
                <p>Discover how SilverCare can support you or your loved one on the journey to better health and independence.</p>
                <div class="about-cta-actions">
                    <a href="serviceCategories" class="button button-primary">Explore Our Services</a>
                    <%
                        if (session.getAttribute("sessUserID") == null) {
                    %>
                        <a href="register" class="button button-secondary">Become a Member</a>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="footer.html" %>

</body>
</html>

