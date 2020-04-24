package com.crud.democrud.auth;

import java.util.HashMap;
import java.util.Map;

import com.crud.democrud.common.JwtTokenProvider;
import com.crud.democrud.user.User;
import com.crud.democrud.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/v1/auth/register")
    public ResponseEntity<Object> registerUser(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        User userObj = mapper.convertValue(body.get("user"), User.class);
        User user = userService.createUser(userObj);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<Object> loginUser(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        User userObj = mapper.convertValue(body.get("user"), User.class);
        User user = userService.verifyUserCredentials(userObj);

        Map<String, Object> map = new HashMap<String, Object>();
        String token = jwtTokenProvider.generateJWT(user);
        map.put("user", user);
        map.put("token", token);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}