package com.tech.cadt.order.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tech.cadt.OrderRepository;
import com.tech.cadt.hystrixdashboard.CreateOrderService;
import com.tech.cadt.hystrixdashboard.GetOrderByCustomer;
import com.tech.cadt.hystrixdashboard.GetOrderService;
import com.tech.cadt.order.entity.OrderProduct;

@RestController
@RequestMapping("/orders")
@Component
public class OrderRestServiceController {
	
	
@Autowired
OrderRepository orderRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestServiceController.class);
    private static final String signingKey = "signingKey";
    
   
	@RequestMapping(value="/create", method = RequestMethod.POST)
	
	public OrderProduct createOrder(@RequestBody OrderProduct order,HttpServletResponse httpServletResponse,@RequestHeader HttpHeaders headers,HttpServletRequest httpServletRequest) {
		CreateOrderService createOrderService = new CreateOrderService();
		createOrderService.execute();
		List<String> token = headers.get("jwtToken");
		String ipAddressOrder =  httpServletRequest.getRemoteAddr();
		
		Map mapValue=new HashMap();		
		mapValue.put("token", token);		
		mapValue.put("ipAddressOrder", ipAddressOrder);		
	
		RestTemplate restTemplate = new RestTemplate();		 
		String res=restTemplate.postForObject("http://localhost:9090/auth-service/loginService/isValid", mapValue, String.class);
		
		System.out.print("inside create");
		
		if(res.equals("validToken")){
			
			order.getOrderDate();
			order = orderRepository.save(order);
		}
		
	    return order;
	}

	
	@RequestMapping("/getOrderByCustomer")	

	
	public List<OrderProduct> getOrderBasedOnCustomerId(@RequestParam long customerId,HttpServletResponse httpServletResponse,@RequestHeader HttpHeaders headers,HttpServletRequest httpServletRequest) {
		List<OrderProduct> getCustomer=null;
		GetOrderByCustomer getOrderByCustomer = new GetOrderByCustomer();
		getOrderByCustomer.execute();
		System.out.println("inside get by customer");
		System.out.println("inside get by customer"+customerId);
		List<String> token = headers.get("jwtToken");
		String ipAddressOrder =  httpServletRequest.getRemoteAddr();
		
		Map mapValue=new HashMap();		
		mapValue.put("token", token);		
		mapValue.put("ipAddressOrder", ipAddressOrder);		
	
		RestTemplate restTemplate = new RestTemplate();		 
		String res=restTemplate.postForObject("http://localhost:9090/auth-service/loginService/isValid", mapValue, String.class);
		
		System.out.print("inside create");
		
		if(res.equals("validToken")){
		getCustomer=orderRepository.findByCustomerId(customerId);	
		}
	    return getCustomer;
		
	    
	}
	@RequestMapping("/getOrder")	
	@ResponseBody 
	public List<OrderProduct> getAll() {
		
		GetOrderService getOrderService = new GetOrderService();
		getOrderService.execute();		
		Iterable<OrderProduct> getCustomer=null;		
		getCustomer=orderRepository.findAll();		
		List<OrderProduct> list = new ArrayList<OrderProduct>();
	    if(getCustomer != null) {
	      for(OrderProduct e: getCustomer) {
	        list.add(e);
	      }
	    }
		
	    return list;
		
	    
	}
	
	
	
		
	}