package com.adp.data.model.customer;
import jakarta.persistence.*;
//import java.time.Instant;

@Entity
@Table(name="customers")
public class Customer {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		private String email; 
		private String password;
		
		public Long getId() {return id;}
		public void setId(Long id) {this.id = id;}
		
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	
		
		
		
	
		

	

}
