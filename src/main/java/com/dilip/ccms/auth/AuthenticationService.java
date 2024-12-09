package com.dilip.ccms.auth;

import com.dilip.ccms.email.EmailService;
import com.dilip.ccms.email.EmailTemplate;
import com.dilip.ccms.role.RoleRepository;
import com.dilip.ccms.security.JwtService;
import com.dilip.ccms.user.Token;
import com.dilip.ccms.user.TokenRepository;
import com.dilip.ccms.user.User;
import com.dilip.ccms.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${spring.mail.frontend.activation-url}")
    private String activateUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER").orElseThrow(
                () -> new IllegalStateException("Role USER was not initialized")
        );
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getUsername(),
                user.getName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activateUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(); // of length 6
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0987654321";
        StringBuilder codeBuilder = new StringBuilder(6);
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 6; i++){
            int index = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }
        return codeBuilder.toString();
    }

    public  AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        log.info("Inside Authentication request");
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("username", user.getUsername());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public void activateAccount(String token) throws MessagingException{
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException(("Invalid Token")));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token is sent to the your email");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
    }

}
