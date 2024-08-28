package com.BlogApplication.security;

import com.BlogApplication.entities.User;
import com.BlogApplication.exceptions.ResourceNotFoundException;
import com.BlogApplication.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load user from db by username
        User user  =this.userRepo.findByEmail(username)
                .orElseThrow(()->new ResourceNotFoundException("user","email"+username,0));
        return user;
    }
}
