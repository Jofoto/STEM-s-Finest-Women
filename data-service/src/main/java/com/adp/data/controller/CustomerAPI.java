package com.adp.data.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adp.data.model.customer.Customer;
import com.adp.data.repository.CustomerRepository;

@RestController
@CrossOrigin
@RequestMapping("/customers")
public class CustomerAPI{
    private final CustomerRepository repo;

    public CustomerAPI(CustomerRepository repo){
        this.repo = repo;
    }
    
    @GetMapping("/search")
    public ResponseEntity<Customer> findByUsername(@RequestParam("username") String username) {
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
            existing.setUsername(c.getUsername());
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
