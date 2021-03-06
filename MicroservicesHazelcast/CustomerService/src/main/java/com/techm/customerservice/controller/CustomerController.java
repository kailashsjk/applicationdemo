package com.techm.customerservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.techm.customerservice.entity.Customer;
import com.techm.customerservice.hystrixdashboard.CreateCustomerService;
import com.techm.customerservice.hystrixdashboard.GetCustomer;
import com.techm.customerservice.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")

public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;
	
	@RequestMapping(value="/create", method = RequestMethod.POST, produces = "application/json")    
	public Customer create(@Valid @RequestBody Customer customer) {
		CreateCustomerService createCustomerService =new CreateCustomerService();
		createCustomerService.execute();	
		customer = customerRepository.save(customer);
	    return customer;
	}
	
	@RequestMapping(value="/getCustomer", method = RequestMethod.POST, produces = "application/json") 
	public Customer getCustomerByName(@Valid @RequestBody Customer customer) {
	GetCustomer getCustomer = new GetCustomer();
	getCustomer.execute();	
	
	Customer cs=new Customer();
	
	cs=customerRepository.findByUserName(customer.getUserName());
	
	return cs;
	}



}
