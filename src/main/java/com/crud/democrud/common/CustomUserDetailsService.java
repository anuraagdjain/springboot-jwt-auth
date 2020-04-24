package com.crud.democrud.common;

import java.util.HashSet;
import java.util.Set;

import com.crud.democrud.user.User;
import com.crud.democrud.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException("Given email not found!!");

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true,
                true, true, buildAuthority());
    }

    private Set<GrantedAuthority> buildAuthority() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return grantedAuthorities;
    }

}