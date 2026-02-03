# SilverCare
This repo is for my JAD assignment 2.

## Stripe Payment Integration
The application uses Stripe for secure payment processing. Bookings are only created in the database after a successful payment is confirmed via Stripe Webhooks.

### Local Development Setup (Webhooks)
To receive payment confirmations on your local machine, you **must** use the Stripe CLI.

1.  **Install Stripe CLI**: Download and install it from the official Stripe docs.
2.  **Login**: Run `stripe login` in your terminal.
3.  **Listen**: Run the following command during development:
    ```bash
    stripe listen --forward-to localhost:8081/api/webhook/stripe
    ```
4.  **Update Secret**: Every time you start `stripe listen`, it may provide a **new** "Webhook signing secret" (starts with `whsec_...`).
    - Copy this secret.
    - Update `stripe.webhook.secret` in `SilverCare-Backend/src/main/resources/application.properties`.
    - **Note**: The general Stripe API Key (`rk_test_...`) stays the same, but this `whsec` key is unique to each listening session or webhook endpoint.

5.  **Restart Backend**: After updating the property file, restart your Spring Boot application.
