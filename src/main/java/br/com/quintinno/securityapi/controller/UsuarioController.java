package br.com.quintinno.securityapi.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<HashMap> getUsuario() {
        HashMap hashMap = new HashMap<>();
            hashMap.put("mensagem", "Requisição Realizada com Sucesso!");
        return ResponseEntity.ok().body(hashMap);
    }

}
