package com.example.nafiz.blog.auth;

import com.example.nafiz.blog.common.ResponseWrapper;
import com.example.nafiz.blog.component.UserInfoUserDetailsService;
import com.example.nafiz.blog.security.JwtService;
import com.example.nafiz.blog.user.UserInfo;
import com.example.nafiz.blog.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserInfoUserDetailsService userDetailsService;

    private final UserRepository userRepository;

    public AuthController(JwtService jwtService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          UserInfoUserDetailsService userDetailsService,
                          UserRepository userRepository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername().toLowerCase(),
                            authRequestDTO.getPassword()
                    )
            );

            if (authenticate.isAuthenticated()) {
                UserInfo user = userRepository.findByUsername(authRequestDTO.getUsername()).orElse(null);
                assert user != null;

                ResponseDTO responseDTO = new ResponseDTO();
                responseDTO.setToken(jwtService.generateToken(
                        authRequestDTO.getUsername().toLowerCase(),
                        (List) userDetailsService.loadUserByUsername(authRequestDTO.getUsername()).getAuthorities()
                ));
                responseDTO.setUsername(authRequestDTO.getUsername().toLowerCase());
                responseDTO.setRoles(jwtService.extractRole(responseDTO.getToken()));
                responseDTO.setExpiredDate(jwtService.extractExpiration(responseDTO.getToken()));
                responseDTO.setUserId(user.getId());

                // ✅ Wrap in status response
                return ResponseEntity.ok(ResponseWrapper.wrap("Login successful", responseDTO));
            } else {
                throw new UsernameNotFoundException("Invalid user request!!");
            }

        } catch (AuthenticationException e) {
            String errorMessage = "Authentication failed: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseWrapper.wrapFailure(errorMessage));
        }
    }


    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(jwtService.extractRole(token.substring(7)), HttpStatus.OK);
    }

//    public void createSysRoot() {
//        if (userRepository.findByUsername("root").isEmpty()) {
//            UserInfo user = new UserInfo();
//            user.setName("admin");
//            user.setUsername("admin");
//            user.setEmail("admin@google.net");
//            user.setPassword(passwordEncoder.encode("admin"));
//            user.setRoles(UserRoles.ROLE_ADMIN);
//
//            userRepository.save(user);
//        }
//    }
}

