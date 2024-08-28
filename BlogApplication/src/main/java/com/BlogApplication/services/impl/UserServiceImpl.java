package com.BlogApplication.services.impl;

import com.BlogApplication.config.AppConstants;
import com.BlogApplication.entities.Role;
import com.BlogApplication.entities.User;
import com.BlogApplication.exceptions.ResourceNotFoundException;
import com.BlogApplication.payloads.UserDto;
import com.BlogApplication.repositories.RoleRepo;
import com.BlogApplication.repositories.UserRepo;
import com.BlogApplication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepo roleRepo;
    @Override
    public UserDto registerNewUser(UserDto userDto) {
     User user=  this.modelMapper.map(userDto,User.class);
     //encoded the password
     user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    //roles
        Role role= this.roleRepo.findById(AppConstants.NORMAL_USER).get();
        user.getRoles().add(role);
        User newUser=this.userRepo.save(user);
     return this.modelMapper.map(newUser,UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userdto) {
        User user=this.dtoToUser(userdto);
        User savedUser=this.userRepo.save(user);
        return this.userTodto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userdto, Integer userId) {
        User user=this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        user.setName(userdto.getName());
        user.setEmail(user.getEmail());
        user.setPassword(userdto.getPassword());
        user.setAbout(userdto.getAbout());

         User updatedUser= this.userRepo.save(user);
         UserDto userdto1=  this.userTodto(updatedUser);
        return userdto1;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user=this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        return this.userTodto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
     List<User> users=   this.userRepo.findAll();
   List<UserDto> userDtos=  users.stream().map(user->this.userTodto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
   User user=  this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
   this.userRepo.delete(user);
    }
    private User dtoToUser(UserDto userdto){
        User user=this.modelMapper.map(userdto,User.class);
        user.setId(userdto.getId());
        user.setName(userdto.getName());
        user.setEmail(userdto.getEmail());
        user.setAbout(userdto.getAbout());
        user.setPassword(userdto.getPassword());
        return user;
    }
    public UserDto userTodto(User user){
        UserDto userdto=this.modelMapper.map(user,UserDto.class);
        userdto.setId(user.getId());
        userdto.setName(user.getName());
        userdto.setEmail(user.getEmail());
        userdto.setPassword(user.getPassword());
        userdto.setAbout(user.getAbout());
        return userdto;
    }
}
