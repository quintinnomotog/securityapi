package br.com.quintinno.securityapi.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quintinno.securityapi.utility.MensagemUtility;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<HashMap<String, String>> getUsuario() {
        HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mensagem", MensagemUtility.MENSAGEM_SUCESSO_01);
        return ResponseEntity.ok().body(hashMap);
    }

}
