package com.alura.forohub.infra.security;

import com.alura.forohub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${jwt.secret}") private String apiSecret;

    public String generarToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getUsername())
                .withExpiresAt(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant())
                .sign(Algorithm.HMAC256(apiSecret));
    }

    public String getSubject(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(apiSecret)).build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}