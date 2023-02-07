package com.densoft.customer.services;

import com.densoft.amqp.RabbitMQMessageProducer;
import com.densoft.clients.fraud.FraudCheckResponse;
import com.densoft.clients.fraud.FraudClient;
import com.densoft.clients.notifications.NotificationClient;
import com.densoft.clients.notifications.NotificationRequest;
import com.densoft.customer.model.Customer;
import com.densoft.customer.payload.CustomerRegistrationRequest;
import com.densoft.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate,
                              FraudClient fraudClient,
                              RabbitMQMessageProducer rabbitMQMessageProducer) {
    public void registerCustomer(CustomerRegistrationRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .build();
        //check if email is valid
        //check if email is not taken
        customerRepository.saveAndFlush(customer);
        //check if fraudster
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }
        //send notification
        NotificationRequest notificationRequest = new NotificationRequest(customer.getId(), customer.getEmail(), String.format("Hi %S, welcome to Densoft Developers", customer.getFirstName()));

        rabbitMQMessageProducer.publish(notificationRequest, "internal.exchange", "internal.notification.routing-key");
    }
}
