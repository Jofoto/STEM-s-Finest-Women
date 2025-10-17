package com.adp.data.controller;

import com.adp.data.model.customer.Customer;
import com.adp.data.repository.CustomerRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerAPI{
    private final CustomerRepository repo;

    public CustomerAPI(CustomerRepository repo){
        this.repo = repo;
    }
    
    @GetMapping("/search")
    public ResponseEntity<Customer> findByUsername(@RequestParam String username) {
    	Optional<Customer> customer = repo.findByUsername(username);
    	return customer.map(ResponseEntity::ok)
    			.orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Customer> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable("id") Long id) {
        return repo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer c) {
        c.setId(null);
        Customer saved = repo.save(c);
        return ResponseEntity.created(URI.create("/api/customers" + saved.getId()))
                             .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable("id") Long id, @RequestBody Customer c) {
        return repo.findById(id).map(existing -> {
            existing.setName(c.getName());
            existing.setEmail(c.getEmail());
            existing.setPassword(c.getPassword());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
