package com.crud.democrud.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Iterable<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User verifyUserCredentials(User user) throws UsernameNotFoundException {
        User userObj = userRepository.findByEmail(user.getEmail());
        if (userObj == null)
            throw new UsernameNotFoundException("Invalid Email");
        if (!bCryptPasswordEncoder.matches(user.getPassword(), userObj.getPassword()))
            throw new UsernameNotFoundException("Invalid Credentials");
        return userObj;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}