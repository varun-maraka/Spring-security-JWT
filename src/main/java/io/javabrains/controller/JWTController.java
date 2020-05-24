package io.javabrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.model.AuthenticationRequest;
import io.javabrains.model.AuthenticationResponse;
import io.javabrains.service.MyUserDetailsService;
import io.javabrains.util.JwtUtil;

@RestController
public class JWTController {
	@Autowired
	private AuthenticationManager authMgr;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping("/hi")
	public String sayHi(){
		return "Hello";
	}
	
	@RequestMapping("/")
	public String allPath(){
		return "Hello, Welcome all";
	}
	
	@RequestMapping("/auth")
	public String allAuth(){
		return "Hello, Welcome Authenticated user";
	}
	
	@RequestMapping("/user")
	public String replyUser(){
		return "Hello, Welcome User";
	}
	
	@RequestMapping("/admin")
	public String replyAdmin(){
		return "Hello, Welcome Admin";
	}
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authReq) throws Exception{
		try{
			authMgr.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		}catch (BadCredentialsException e){
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	
	}
}
