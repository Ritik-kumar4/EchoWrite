package com.BlogApplication.controllers;

import com.BlogApplication.entities.User;
import com.BlogApplication.exceptions.ApiException;
import com.BlogApplication.payloads.JwtAuthRequest;
import com.BlogApplication.payloads.JwtAuthResponse;
import com.BlogApplication.payloads.UserDto;
import com.BlogApplication.repositories.UserRepo;
import com.BlogApplication.security.JwtTokenHelper;
import com.BlogApplication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ) throws Exception {
        this.authenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails= this.userDetailsService.loadUserByUsername(request.getUsername());
      String token=  this.jwtTokenHelper.generateToken(userDetails);
      JwtAuthResponse response=new JwtAuthResponse();
      response.setToken(token);
      return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }
    private void authenticate(String username, String password) throws Exception{
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            System.out.println("Invalid Details...");
            throw new ApiException("Invalid username or password...");
        }
    }
    //register new user api

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
      UserDto registeredUser=  this.userService.registerNewUser(userDto);

      return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
    }

    // get loggedin user data
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;

//    @GetMapping("/current-user")
//    public ResponseEntity<UserDto> getUser(Principal principal) {
//        if (principal == null || principal.getName() == null) {
//            // Handle case where principal is null or its name is null
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Or handle as appropriate
//        }
//
//        // Now it's safe to access the principal's name
//        User user = this.userRepo.findByEmail(principal.getName()).orElse(null);
//        if (user == null) {
//            // Handle case where user is not found
//            return ResponseEntity.notFound().build(); // Or handle as appropriate
//        }
//
//        return ResponseEntity.ok(this.mapper.map(user, UserDto.class));
//    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDto> getUser(Principal principal) {
        User user = this.userRepo.findByEmail(principal.getName()).get();
        return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
    }
}
