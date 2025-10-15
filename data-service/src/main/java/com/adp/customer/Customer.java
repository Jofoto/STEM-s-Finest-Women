package com.adp.customer;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="customers")
public class Customer {

	public static void main(String[] args) {
		@Id
		@GeneratedCalue(strategy = GenerationType.IDENTITY)
		private Long id;

	}

}
