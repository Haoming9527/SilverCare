package com.SilverCare.SilverCare_Backend.service;

import com.SilverCare.SilverCare_Backend.dbaccess.Booking;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.client.url}")
    private String clientUrl;

    @Value("${stripe.tax.rate.id}")
    private String taxRateId;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public Session createCheckoutSession(Booking booking) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(clientUrl + "/paymentSuccess.jsp")
                .setCancelUrl(clientUrl + "/bookingForm.jsp?service_id=" + booking.getServiceId() + "&service_name=" + URLEncoder.encode(booking.getServiceName(), StandardCharsets.UTF_8))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .addTaxRate(taxRateId)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("sgd")
                                                .setUnitAmount((long) (booking.getPrice() * 100)) // Amount in cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                 .setName(booking.getServiceName())
                                                                 .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("user_id", String.valueOf(booking.getUserId()))
                .putMetadata("service_id", String.valueOf(booking.getServiceId()))
                .putMetadata("scheduled_date", booking.getScheduledDate())
                .putMetadata("specific_caregiver", booking.getSpecificCaregiver() != null ? booking.getSpecificCaregiver() : "")
                .putMetadata("special_request", booking.getSpecialRequest() != null ? booking.getSpecialRequest() : "")
                .build();

        return Session.create(params);
    }
}
