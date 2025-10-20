package com.adp.data.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adp.data.model.customer.Customer;
import com.adp.data.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CustomerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository repo;

    @Test
    @DisplayName("POST /api/customers should create a customer")
    void createCustomer() throws Exception {
        Customer c = new Customer();
        c.setUsername("Alice");
        c.setEmail("alice@adp.com");
        c.setPassword("alice123");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email").value("alice@adp.com"));
    }

    @Test
    @DisplayName("GET /api/customers should return list of customers")
    void getCustomers() throws Exception {
        Customer c = new Customer();
        c.setUsername("Bob");
        c.setEmail("bob@adp.com");
        c.setPassword("bob123");
        repo.save(c);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("PUT /api/customers/{id} should update customer")
    void updateCustomer() throws Exception {
        Customer c = new Customer();
        c.setUsername("Maria");
        c.setEmail("maria@adp.com");
        c.setPassword("maria123");
        Customer saved = repo.save(c);

        Customer updated = new Customer();
        updated.setUsername("Maria Updated");
        updated.setEmail("maria2@adp.com");
        updated.setPassword("marianew");

        mockMvc.perform(put("/customers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Maria Updated"));
    }

    @Test
    @DisplayName("DELETE /api/customers/{id} should delete customer")
    void deleteCustomer() throws Exception {
        Customer c = new Customer();
        c.setUsername("Dave");
        c.setEmail("dave@adp.com");
        c.setPassword("daveee");
        Customer saved = repo.save(c);

        mockMvc.perform(delete("/customers/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}