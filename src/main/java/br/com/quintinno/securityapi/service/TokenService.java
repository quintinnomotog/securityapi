package br.com.quintinno.securityapi.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.quintinno.securityapi.entity.UsuarioEntity;

@Service
public class TokenService {

    @Value("${spring.application.name}")
    private String apiName;

    @Value("${api.securityapi.token.secret}")
    private String secret;

    public String generateToken(UsuarioEntity usuarioEntity) {
        try {
            Algorithm algorithm = getAlgorithm();
            String token = JWT.create()
                    .withIssuer(apiName)
                    .withSubject(usuarioEntity.getIdentificador())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar Token!");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = getAlgorithm();
            return JWT.require(algorithm)
                    .withIssuer(apiName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException jwtVerificationException) {
            return null;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

}
