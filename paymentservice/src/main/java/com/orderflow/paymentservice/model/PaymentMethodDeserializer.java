package com.orderflow.paymentservice.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class PaymentMethodDeserializer extends JsonDeserializer<PaymentMethod> {
    @Override
    public PaymentMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return PaymentMethod.fromString(value);
    }
}

