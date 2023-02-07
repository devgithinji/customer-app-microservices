package com.densoft.customer.payload;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
