package com.crud.democrud.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/v1/users")
    public ResponseEntity<Object> listAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/v1/me")
    public ResponseEntity<Object> getMyInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}