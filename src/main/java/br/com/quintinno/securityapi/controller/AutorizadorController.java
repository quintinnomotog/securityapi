package br.com.quintinno.securityapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quintinno.securityapi.service.AutenticadorService;
import br.com.quintinno.securityapi.transfer.SigninRequestTransfer;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;

@RestController
@RequestMapping("/autorizador")
public class AutorizadorController {

    @Autowired
    private AutenticadorService autenticadorService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequestTransfer loginRequestTransfer) {
        try {
            return ResponseEntity.ok(this.autenticadorService.signin(loginRequestTransfer));
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(runtimeException.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequestTransfer signupRequestTransfer) {
        try {
            return ResponseEntity.ok(this.autenticadorService.signup(signupRequestTransfer));
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(runtimeException.getMessage());
        }
    }

}
