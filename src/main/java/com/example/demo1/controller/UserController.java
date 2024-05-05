package com.example.demo1.controller;

import com.example.demo1.entity.AuthRequest;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepo;
import com.example.demo1.service.JwtService;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepo Rservice;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "home";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @GetMapping("/user/me")
    public ResponseEntity<User> getMe() {

        return new ResponseEntity<User>(HttpStatus.OK);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "userProfile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> listAllUser(){
        List<User> listUser = Rservice.findAll();
        if (listUser.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(listUser, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        User user = Rservice.findById(id).get();

        if (!Rservice.existsById(id)) {
            ResponseEntity.notFound().  build();
        }

        return user;
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateContact(@PathVariable(value = "id") Integer id,
                                              @RequestBody User contactForm) {
        User user = Rservice.findById(id).get();
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setName(contactForm.getName());
        user.setAddress(contactForm.getAddress());
        user.setEmail(contactForm.getEmail());
        user.setPhonenum(contactForm.getPhonenum());

        User updatedContact = Rservice.save(user);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Integer id) {
        if (!Rservice.existsById(id)) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        Rservice.deleteById(id);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
    }
}