package com.scaler.assignment.usermgmtservice.services;

import com.scaler.assignment.usermgmtservice.models.TokenData;
import com.scaler.assignment.usermgmtservice.models.User;
import com.scaler.assignment.usermgmtservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("classpath:certs/private_key.pem")
    private RSAPrivateKey privateKey;

    @Value("classpath:certs/public_key.pem")
    private RSAPublicKey publicKey;

    public TokenData login(final String email, final String password) throws IllegalAccessException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalAccessException("User not registered"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalAccessException("Wrong password");
        }
        long exp = System.currentTimeMillis() + (15 * 60 * 1000);
        String jwt = Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuer("USER_API")
                .expiration(new Date(exp))
                .issuedAt(new Date())
                .signWith(privateKey)
                .compact();
        return new TokenData(jwt, exp, "JWT");
    }

    public boolean verify(final String jwt) {
        Jws<Claims> claimsJws = Jwts.parser()
                .requireIssuer("USER_API")
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(jwt);

        var uuid = UUID.fromString(claimsJws.getPayload().getSubject());
        return userRepository.findById(uuid)
                .filter(user -> user.getEmail().equals(claimsJws.getPayload().get("email")))
                .isPresent();
    }
}
