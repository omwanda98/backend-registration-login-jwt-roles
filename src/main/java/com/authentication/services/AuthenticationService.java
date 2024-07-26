package com.authentication.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authentication.models.ApplicationUser;
import com.authentication.models.LoginResponseDTO;
import com.authentication.models.Role;
import com.authentication.repository.RoleRepository;
import com.authentication.repository.UserRepository;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public ApplicationUser registerUser(String username, String password) throws RuntimeException {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        System.out.println("---------------IN--------------------------");
        System.out.println(username + " " + password);
        System.out.println("---------------OUT--------------------------");
        authorities.add(userRole);
        System.out.println("here1");
        var res = userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities));
        System.out.println("here2");
        return res;
    }

    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);
            var user = userRepository.findByUsername(username).get();
            System.out.println("--------- User --------------");
            System.out.println(user.getUsername());
            System.out.println("--------- User Out --------------");

            return new LoginResponseDTO(user, token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }

}
