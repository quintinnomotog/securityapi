package br.com.quintinno.securityapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quintinno.securityapi.service.SigninService;
import br.com.quintinno.securityapi.transfer.SigninRequestTransfer;

@RestController
@RequestMapping("/signin")
public class SigninController {

    @Autowired
    private SigninService signinService;

    @PostMapping
    public ResponseEntity<?> signin(@RequestBody SigninRequestTransfer loginRequestTransfer) {
        try {
            return ResponseEntity.ok(this.signinService.signin(loginRequestTransfer));
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(runtimeException.getMessage());
        }
    }

}
