<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Home</title>
        <link rel="stylesheet" type="text/css" href="styles.css?v=2">
    </head>

    <body>

        <%@ include file="header.jsp" %>

            <main>
                <section class="hero">
                    <div class="hero-content container">
                        <h2>Compassionate Care for Every Stage of Aging</h2>
                        <p>
                            SilverCare provides professional in-home support, assisted living services, and personalized
                            therapy
                            so seniors can thrive with confidence and dignity.
                        </p>
                        <div class="hero-actions">
                            <a href="serviceCategories" class="button button-primary">Explore Services</a>
                            <% if (session.getAttribute("sessUserID")==null) { %>
                                <a href="register" class="button button-secondary">Become a Member</a>
                                <% } %>
                        </div>
                    </div>
                </section>

                <section class="features container">
                    <article class="feature-card">
                        <h3>Trusted Professionals</h3>
                        <p>Certified caregivers, nurses, and therapists carefully matched to every client’s needs.</p>
                    </article>
                    <article class="feature-card">
                        <h3>Flexible Scheduling</h3>
                        <p>Book one-time visits, recurring care, or on-call assistance that fits your routine.</p>
                    </article>
                    <article class="feature-card">
                        <h3>Holistic Wellness</h3>
                        <p>From daily living support to therapy and companionship, we care for mind and body.</p>
                    </article>
                </section>
            </main>

            <%@ include file="footer.html" %>

    </body>

    </html>