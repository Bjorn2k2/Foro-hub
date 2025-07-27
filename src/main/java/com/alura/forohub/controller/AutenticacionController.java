package com.alura.forohub.controller;

import com.alura.forohub.domain.usuario.dto.DatosAutenticacionUsuario;
import com.alura.forohub.domain.usuario.Usuario;
import com.alura.forohub.infra.security.TokenService;
import com.alura.forohub.infra.security.DatosTokenJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(datos.login(), datos.clave())
        );
        String jwt = tokenService.generarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(jwt));
    }
}